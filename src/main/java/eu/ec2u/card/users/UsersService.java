package eu.ec2u.card.users;

import com.google.cloud.datastore.*;
import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import eu.ec2u.card.ToolApplication;
import eu.ec2u.card.users.Users.User;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UsersService {

    @Autowired private UsersRepository usersRepository;
    @Autowired private DatastoreTemplate datastoreTemplate;

    @SuppressWarnings("ALL")
    Users browse(

            final Optional<String> label,
            final Optional<String> email,
            final Pageable slice

    ) {

        final Users users = new Users();
        users.setPath(Users.Id);

        Query<Entity> query;

        if (label.isPresent() && email.isPresent()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("label", label.get().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("label", label.get().toLowerCase() + "\ufffd"),
                            StructuredQuery.PropertyFilter.ge("email", email.get()),
                            StructuredQuery.PropertyFilter.lt("email", email.get() + "\ufffd")
                    ))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (label.isEmpty() && email.isPresent()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("email", email.get()),
                            StructuredQuery.PropertyFilter.lt("email", email.get() + "\ufffd")
                    ))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (label.isPresent()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("label", label.get().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("label", label.get().toLowerCase() + "\ufffd")
                    ))
                    .setLimit(slice.getPageSize())
                    .build();

        } else {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setLimit(slice.getPageSize())
                    .build();

        }

        List<User> userList = new ArrayList<>();

        this.datastoreTemplate.query(query, UserData.class)
                .toList()
                .forEach(userData -> userList.add(userData.transfer()));

        users.setContains(userList);

        return users;

    }

    @Transactional
    User create(final User user) {

        String errorMessage = PostAndPutArgumentsErrorMessageGenerator(
                user.getForename(), user.getSurname(), user.getEmail());

        if (!errorMessage.equals("")) {

            throw new ToolApplication.WrongPostArgumentsException(errorMessage);

        } else {

            return Optional.of(new UserData())
                    .map(data -> data.transfer(user))
                    .map(data -> usersRepository.save(data))
                    .map(UserData::transfer)
                    .orElseThrow(NoSuchElementException::new);

        }

    }

    User relate(final long id) {

        return usersRepository.findById(id)
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    @Transactional
    User update(final long id, final User user) {

        String errorMessage = PostAndPutArgumentsErrorMessageGenerator(
                user.getForename(), user.getSurname(), user.getEmail());

        if (!errorMessage.equals("")) {

            throw new ToolApplication.WrongPutArgumentsException(errorMessage);

        } else {

            return usersRepository.findById(id)
                    .map(data -> data.transfer(user))
                    .map(data -> usersRepository.save(data))
                    .map(UserData::transfer)
                    .orElseThrow(NoSuchElementException::new);

        }

    }

    @Transactional
    User delete(final long id) {

        return usersRepository.findById(id)
                .map(data -> {

                    usersRepository.delete(data);

                    return data;

                })
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

   ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String PostAndPutArgumentsErrorMessageGenerator(

            String forenamePrefix,
            String surnamePrefix,
            String emailPrefix

    ) {

        String errorMessage = "";

        if(!forenamePrefix.equals("") && !Pattern.compile("^[a-zA-Z]+$").matcher(forenamePrefix).matches()) {

            errorMessage += "Forename is not valid! Must follow ^[a-zA-Z]+$ regex pattern. \n";

        }

        if(!surnamePrefix.equals("") && !Pattern.compile("^[a-zA-Z]+$").matcher(surnamePrefix).matches()) {

            errorMessage += "Surname is not valid! Must follow ^[a-zA-Z]+$ regex pattern. \n";

        }

        if(!emailPrefix.equals("") && !Pattern.compile("^[a-z]+@[a-z]+\\.[a-z]+$").matcher(emailPrefix).matches()) {

            errorMessage += "Email is not valid! Must follow ^[a-z]+@[a-z]+\\.[a-z]+$ regex pattern. \n";

        }

        return errorMessage;

    }

}
