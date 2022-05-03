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

        // Queries are designed to work with only one parameter at time!

        final Users users = new Users();
        users.setPath(Users.Id);

        Query<Entity> query = null;

        if (label.isPresent() && email.isEmpty()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("label", label.get().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("label", label.get().toLowerCase() + "\ufffd")
                    ))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (label.isEmpty() && email.isPresent()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("email", email.get().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("email", email.get().toLowerCase() + "\ufffd")
                    ))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (label.isEmpty() && email.isEmpty()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setLimit(slice.getPageSize())
                    .build();

        } else {

            throw new ToolApplication.WrongQueryArgumentsException(
                    "Queries are designed to work with only one parameter at time!");

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

        if (!isEmailArgumentValid(user.getEmail())) {

            throw new ToolApplication.WrongPostArgumentsException(
                    "Email argument is not valid, must follow ^[a-z]+@[a-z]+\\.[a-z]+$ regex pattern!");

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

        if (!isEmailArgumentValid(user.getEmail())) {

            throw new ToolApplication.WrongPostArgumentsException(
                    "Email argument is not valid, must follow ^[a-z]+@[a-z]+\\.[a-z]+$ regex pattern!");

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

    @SuppressWarnings("ALL")
    private boolean isEmailArgumentValid(final String email) {

        return Pattern.compile("^[a-z]+@[a-z]+\\.[a-z]+$").matcher(email).matches();

    }

}
