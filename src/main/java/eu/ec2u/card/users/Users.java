package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.*;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
public class Users extends Container<User> {

    static final String Id="/users/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Jacksonized
    @SuperBuilder
    static final class User extends Resource {

        private final boolean admin;

        private final String forename;
        private final String surname;

        private final String email;

    }

    @Getter
    @Entity(name=Id)
    static final class UserData extends ResourceData {

        private boolean admin;

        private String forename;
        private String surname;

        private String email;


        User transfer() {
            return User.builder()

                    .id(Id+id)

                    .admin(admin)

                    .forename(forename)
                    .surname(surname)

                    .email(email)

                    .build();
        }

        UserData transfer(final User user) {

            admin=user.isAdmin();

            forename=user.getForename();
            surname=user.getSurname();

            email=user.getEmail();

            return this;

        }

    }

}
