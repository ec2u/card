package eu.ec2u.card.users;

import com.google.cloud.datastore.*;
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

    Users browse(final Pageable slice) {

        final Users users = new Users();

        users.setPath(Users.Id);

        users.setContains(this.usersRepository.findAll(slice)
                .map(UserData::transfer)
                .getContent()
        );

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

    Users search(

            final Optional<String> forenamePrefix,
            final Optional<String> surnamePrefix,
            final Optional<String> emailPrefix,
            final Pageable slice

    ) {
        
        final Users users = new Users();

        users.setPath(Users.Id);

        List<User> userList = new ArrayList<>();

        String label = forenamePrefix.orElse("").toLowerCase() + surnamePrefix.orElse("").toLowerCase();

        Query<Entity> query;

        if ((forenamePrefix.isPresent() || surnamePrefix.isPresent()) && emailPrefix.isPresent())  {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(

                            StructuredQuery.PropertyFilter.ge("label", label),
                            StructuredQuery.PropertyFilter.lt("label", label + "\ufffd"),
                            StructuredQuery.PropertyFilter.ge("email", emailPrefix.get()),
                            StructuredQuery.PropertyFilter.lt("email", emailPrefix.get() + "\ufffd")

                    ))
                    .setOrderBy(StructuredQuery.OrderBy.asc("label"))
                    .build();

        } else if (forenamePrefix.isEmpty() && surnamePrefix.isEmpty() && emailPrefix.isPresent()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(

                            StructuredQuery.PropertyFilter.ge("email", emailPrefix.get()),
                            StructuredQuery.PropertyFilter.lt("email", emailPrefix.get() + "\ufffd")

                    ))
                    .setOrderBy(StructuredQuery.OrderBy.asc("label"))
                    .build();

        } else {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setOrderBy(StructuredQuery.OrderBy.asc("label"))
                    .build();

        }


        QueryResults<Entity> results = usersRepository.run(query);


//
//        if(forenamePrefix.isEmpty() && surnamePrefix.isEmpty() && emailPrefix.isEmpty()) {
//
//            userDataSet.addAll(this.usersRepository.findAllUserData(slice));
//
//        }
//
//        userDataSet.forEach(userData -> userList.add(userData.transfer()));
//        userList.sort(Comparator.comparing(user -> user.getSurname().toLowerCase()));

        users.setContains(userList);

        return users;

    }

    private String GetArgumentsErrorMessageGenerator(

            String forenamePrefix,
            String surnamePrefix,
            String emailPrefix

    ) {

        String errorMessage = "";

       if(!forenamePrefix.equals("") && !Pattern.compile("^[a-zA-Z]+$").matcher(forenamePrefix).matches()) {

           errorMessage += "Forename prefix is not valid! Must follow ^[a-zA-Z]+$ regex pattern. \n";

       };

        if(!surnamePrefix.equals("") && !Pattern.compile("^[a-zA-Z]+$").matcher(surnamePrefix).matches()) {

            errorMessage += "Surname prefix is not valid! Must follow ^[a-zA-Z]+$ regex pattern. \n";

        };

        if(!emailPrefix.equals("") && !Pattern.compile("^[a-z]+$").matcher(emailPrefix).matches()) {

            errorMessage += "Email prefix is not valid! Must follow ^[a-z]+$ regex pattern. \n";

        };

        return errorMessage;

    }

    private String PostAndPutArgumentsErrorMessageGenerator(

            String forenamePrefix,
            String surnamePrefix,
            String emailPrefix

    ) {

        String errorMessage = "";

        if(!forenamePrefix.equals("") && !Pattern.compile("^[a-zA-Z]+$").matcher(forenamePrefix).matches()) {

            errorMessage += "Forename is not valid! Must follow ^[a-zA-Z]+$ regex pattern. \n";

        };

        if(!surnamePrefix.equals("") && !Pattern.compile("^[a-zA-Z]+$").matcher(surnamePrefix).matches()) {

            errorMessage += "Surname is not valid! Must follow ^[a-zA-Z]+$ regex pattern. \n";

        };

        if(!emailPrefix.equals("") && !Pattern.compile("^[a-z]+@[a-z]+\\.[a-z]+$").matcher(emailPrefix).matches()) {

            errorMessage += "Email is not valid! Must follow ^[a-z]+@[a-z]+\\.[a-z]+$ regex pattern. \n";

        };

        return errorMessage;

    }

}
