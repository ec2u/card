package eu.ec2u.card.users;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class UsersService {

    Users browse(final Pageable slice) {
        return Users.builder()

                .id(Users.Path)

                .contains(List.of())

                //.users(users.findAll(slice).<User>map(data -> data.digest().build()).getContent())

                .build();
    }

}
