/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.users;

import eu.ec2u.card.RootServer;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import javax.json.*;
import javax.validation.constraints.*;

import static eu.ec2u.card.Root.Container;
import static eu.ec2u.card.Root.Resource;

import static javax.json.JsonValue.NULL;

@Getter
@Setter
public class Users extends Container<User> {

    @Getter
    @Setter
    public static final class User extends Resource {

        @NotNull
        @Size(max=RootServer.URLSize)
        @Pattern(regexp=RootServer.ESIPattern)
        private String esi;

        @NotNull
        private Boolean admin;


        @NotNull
        @Size(max=RootServer.LineSize)
        @Pattern(regexp=RootServer.LinePattern)
        private String forename;

        @NotNull
        @Size(max=RootServer.LineSize)
        @Pattern(regexp=RootServer.LinePattern)
        private String surname;


        @NotNull
        @Size(max=RootServer.LineSize)
        @Email
        private String email;


        public static JsonValue encode(final User user) {
            return user == null ? NULL : Json.createObjectBuilder()
                    .add("esi", user.esi)
                    .build();
        }

        public static User decode(final JsonObject json) {
            return json == null || json == NULL ? null : new User()
                    .setEsi(json.getString("esi"));
        }

    }

}
