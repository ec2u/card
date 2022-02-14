package eu.ec2u.card.users;

import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.events.EventsRepository;
import eu.ec2u.card.users.Users.User;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static eu.ec2u.card.events.Events.Action.Created;

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
    String create(final Profile profile, final User user) {

        return events.trace(profile, Created, Users.Id,
                users.save(new UserData().transfer(user)).transfer()
        );

    }

}
