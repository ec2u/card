package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.*;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import javax.validation.constraints.*;

import static eu.ec2u.card.ToolConfiguration.LabelPattern;
import static eu.ec2u.card.ToolConfiguration.LabelSize;

import static java.lang.String.format;

@Getter
@Setter
public class Users extends Container<User> {

    static final String Id="/users/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    static final class User extends Resource {

        @NotNull
        private Boolean admin;

        @NotNull
        @Size(max=LabelSize)
        @Pattern(regexp=LabelPattern)
        private String forename;

        @NotNull
        @Size(max=LabelSize)
        @Pattern(regexp=LabelPattern)
        private String surname;

        @NotNull
        @Size(max=LabelSize)
        @Email
        private String email;

    }

    @Entity(name="User")
    static final class UserData extends ResourceData {

        private Optional<String> id() {
            return Optional.of(this)
                    .filter(data -> data.id != null)
                    .map(data -> Id+data.id);
        }

        private Optional<String> title() {
            return Optional.of(this)
                    .filter(data -> data.forename != null)
                    .filter(data -> data.surname != null)
                    .map(data -> format("%s %s", data.forename, data.surname));
        }


        private boolean admin;

        private String forename;
        private String surname;

        private String email;


        User transfer() {

            final User user=new User();

            id().ifPresent(user::setId);
            title().ifPresent(user::setTitle);

            user.setDescription(description);

            user.setAdmin(admin);

            user.setForename(forename);
            user.setSurname(surname);

            user.setEmail(email);

            return user;

        }

        UserData transfer(final User user) {

            if ( !id().equals(Optional.ofNullable(user.getId())) ) {
                throw new IllegalStateException("mutated value for read-only field <id>");
            }

            if ( !title().equals(Optional.ofNullable(user.getTitle())) ) {
                throw new IllegalStateException("mutated value for read-only field <title>");
            }

            description=user.getDescription();

            admin=user.getAdmin();

            forename=user.getForename();
            surname=user.getSurname();

            email=user.getEmail();

            return this;

        }

    }

}
