package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.*;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

import static eu.ec2u.card.ToolConfiguration.LabelPattern;
import static eu.ec2u.card.ToolConfiguration.LabelSize;

@Getter
@Setter
public class Users extends Container<User> {

    static final String Id="/users/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    static final class User extends Resource {

        @NotNull(groups=Resource.class)
        private Boolean admin;

        @Size(max=LabelSize)
        @Pattern(regexp=LabelPattern)
        @NotNull(groups=Resource.class)
        private String forename;

        @Size(max=LabelSize)
        @Pattern(regexp=LabelPattern)
        @NotNull(groups=Resource.class)
        private String surname;

        @Email
        @Size(max=LabelSize)
        @NotNull(groups=Resource.class)
        private String email;

    }

    @Entity(name="User")
    static final class UserData extends ResourceData {

        private boolean admin;

        private String forename;
        private String surname;

        private String email;


        User transfer() {

            final User user=new User();

            user.setId(Id+id);

            user.setAdmin(admin);

            user.setForename(forename);
            user.setSurname(surname);

            user.setEmail(email);

            return user;

        }

        UserData transfer(final User user) {

            admin=user.getAdmin();

            forename=user.getForename();
            surname=user.getSurname();

            email=user.getEmail();

            return this;

        }

    }

}
