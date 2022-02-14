package eu.ec2u.card.users;

import eu.ec2u.card.events.EventsRepository;
import eu.ec2u.card.users.Users.User;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired private UsersRepository users;
    @Autowired private EventsRepository events;


    Users browse(final Pageable slice) {

        final Users users=new Users();

        users.setId(Users.Id);

        users.setContains(this.users.findAll(slice)
                .map(UserData::transfer)
                .getContent()
        );

        return users;

    }

    @Transactional
    Optional<User> create(final User user) {
        return Optional.of(new UserData())
                .map(data -> data.transfer(user))
                .map(data -> users.save(data))
                .map(UserData::transfer);
    }

    Optional<User> relate(final long id) {
        return users.findById(id).map(UserData::transfer);
    }

    Optional<User> update(final long id, final User user) {
        return users.findById(id)
                .map(data -> data.transfer(user))
                .map(data -> users.save(data))
                .map(UserData::transfer);
    }

    Optional<User> delete(final long id) {
        return users.findById(id)
                .map(data -> {

                    users.delete(data);

                    return data;

                })
                .map(UserData::transfer);
    }

}
