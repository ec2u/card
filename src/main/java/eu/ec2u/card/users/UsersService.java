package eu.ec2u.card.users;

import eu.ec2u.card.users.Users.User;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired private UsersRepository users;

    Users browse(final Pageable slice) {

        final Users users = new Users();

        users.setPath(Users.Id);

        users.setContains(this.users.findAll(slice)
                .map(UserData::transfer)
                .getContent()
        );

        return users;

    }

    @Transactional
    User create(final User user) {

        return Optional.of(new UserData())
                .map(data -> data.transfer(user))
                .map(data -> users.save(data))
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    User relate(final long id) {

        return users.findById(id)
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    @Transactional
    User update(final long id, final User user) {

        return users.findById(id)
                .map(data -> data.transfer(user))
                .map(data -> users.save(data))
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    @Transactional
    User delete(final long id) {

        return users.findById(id)
                .map(data -> {

                    users.delete(data);

                    return data;

                })
                .map(UserData::transfer)
                .orElseThrow(NoSuchElementException::new);

    }

    Users searchBySurnamePrefix(final String surnamePrefix, final Pageable slice) {

        final Users users = new Users();

        users.setPath(Users.Id);

        users.setContains(this.users.findBySurnameStartingWith(surnamePrefix, slice)
                .stream()
                .map(UserData::transfer)
                .collect(Collectors.toList()) );

        return users;

    }

    Users searchBySurnamePrefix(final String surnamePrefix) {

        final Users users = new Users();

        users.setPath(Users.Id);

        users.setContains(this.users.findBySurnameStartingWith(surnamePrefix)
                .stream()
                .map(UserData::transfer)
                .collect(Collectors.toList()) );

        return users;

    }

    Users searchBySurnamePrefixAlternative(final String surnamePrefix) {

        final Users users = new Users();

        final List<User> userList = new ArrayList<>();

        users.setPath(Users.Id);

        this.users.findAll().forEach(userData -> {

            User user = userData.transfer();

            if (user.getSurname().startsWith(surnamePrefix)) {

               userList.add(user);

            }

        });

        users.setContains(userList);

        return users;

    }

}
