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

@Service
public class UsersService {

    @Autowired private UsersRepository usersRepository;

    @Autowired private DatastoreTemplate datastoreTemplate;

    @SuppressWarnings("ALL")
    Users browse(

            final Optional<String> forename,
            final Optional<String> surname,
            final Optional<String> email,
            final Optional<Boolean> isAdmin,
            final Pageable slice,
            final Optional<String> sortingOrder,
            final Optional<String> sortingProperty

    ) {

        // Queries are designed to work with only one parameter at time!

        final Users users = new Users();
        users.setPath(Users.Id);

        Query<Entity> query = null;

        if (forename.isPresent() && surname.isEmpty() && email.isEmpty() && isAdmin.isEmpty()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("forenameLowerCase", forename.get().trim().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("forenameLowerCase", forename.get().trim().toLowerCase() + "\ufffd")
                    ))
                    .setOrderBy(sortingFromOptional(sortingOrder, "forenameLowerCase"))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (forename.isEmpty() && surname.isPresent() && email.isEmpty() && isAdmin.isEmpty()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("surnameLowerCase", surname.get().trim().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("surnameLowerCase", surname.get().trim().toLowerCase() + "\ufffd")
                    ))
                    .setOrderBy(sortingFromOptional(sortingOrder, "surnameLowerCase"))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (forename.isEmpty() && surname.isEmpty() && email.isPresent() && isAdmin.isEmpty()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.CompositeFilter.and(
                            StructuredQuery.PropertyFilter.ge("email", email.get().trim().toLowerCase()),
                            StructuredQuery.PropertyFilter.lt("email", email.get().trim().toLowerCase() + "\ufffd")
                    ))
                    .setOrderBy(sortingFromOptional(sortingOrder, "email"))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if(forename.isEmpty() && surname.isEmpty() && email.isEmpty() && isAdmin.isPresent()) {

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.PropertyFilter.eq("admin", isAdmin.get()))
                    .setLimit(slice.getPageSize())
                    .build();

        } else if (forename.isEmpty() && surname.isEmpty() && email.isEmpty() && isAdmin.isEmpty()) {

            if (!isSortingPropertyValid(sortingProperty)) {

                throw new ToolApplication.WrongQueryArgumentsException(
                        "Sorting property parameter incorrect. Must be forenameLowerCase, surnameLowerCase or email!");

            }

            query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setOrderBy(sortingFromOptional(sortingOrder, sortingProperty.orElse("surnameLowerCase").trim()))
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

            return Optional.of(new UserData())
                    .map(data -> data.transfer(user))
                    .map(data -> usersRepository.save(data))
                    .map(UserData::transfer)
                    .orElseThrow(NoSuchElementException::new);

    }

    User relate(final long id) {

        return usersRepository.findById(id)
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    @Transactional
    User update(final long id, final User user) {

            return usersRepository.findById(id)
                    .map(data -> data.transfer(user))
                    .map(data -> usersRepository.save(data))
                    .map(UserData::transfer)
                    .orElseThrow(NoSuchElementException::new);

    }

    @Transactional
    User delete(final long id) {

        return usersRepository.findById(id)
                .map(data -> {
                    usersRepository.delete(data);
                    return data;
                }).map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("ALL")
    private StructuredQuery.OrderBy sortingFromOptional(Optional<String> sortingOrder, String sortingProperty) {

        if(sortingOrder.isPresent()) {

            if (sortingOrder.get().trim().equalsIgnoreCase("asc")) {

                return StructuredQuery.OrderBy.asc(sortingProperty);

            } else if (sortingOrder.get().trim().equalsIgnoreCase("desc")) {

                return StructuredQuery.OrderBy.desc(sortingProperty);

            } else throw new ToolApplication.WrongQueryArgumentsException(
                    "Specified sorting order is invalid, must be asc or desc!");

        } else {

            return StructuredQuery.OrderBy.asc(sortingProperty);

        }

    }

    private boolean isSortingPropertyValid(Optional<String> sortingProperty) {

        return sortingProperty.map(s -> s.trim().equalsIgnoreCase("forenameLowerCase") ||
                s.trim().equalsIgnoreCase("surnameLowerCase") ||
                s.trim().equalsIgnoreCase("email")).orElse(true);

    }

}
