/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.users;

import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import javax.json.*;
import javax.validation.constraints.*;

import static eu.ec2u.card.Card.*;

import static java.lang.String.format;

import static javax.json.JsonValue.NULL;

@Getter
@Setter
public class Users extends Container<User> {

    static final String Id="/users/";


    public static JsonValue encode(final User user) {
        return user == null ? NULL : Json.createObjectBuilder()
                .add("esi", user.esi)
                .build();
    }

    public static User decode(final JsonObject json) {
        return new User()
                .setEsi(json.getString("esi"));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class User extends Resource {

        @NotNull
        @Size(max=URLSize)
        @Pattern(regexp=RelativePattern) // !!! ESI Pattern
        private String esi;

        @NotNull
        private Boolean admin;


        @NotNull
        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String forename;

        @NotNull
        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String surname;


        @NotNull
        @Size(max=LineSize)
        @Email
        private String email;

    }

    static final class UserData extends ResourceData {

        protected Optional<String> getPath() {
            return Optional.of(this)
                    .filter(data -> data.id != null)
                    .map(data -> Id+data.id);
        }

        protected Optional<String> getLabel() {
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

            transfer(user, this);

            user.setAdmin(admin);

            user.setForename(forename);
            user.setSurname(surname);

            user.setEmail(email);

            return user;

        }

        UserData transfer(final User user) {

            transfer(this, user);

            admin=user.getAdmin();

            forename=user.getForename();
            surname=user.getSurname();

            email=user.getEmail();

            return this;

        }

    }

}
