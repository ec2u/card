/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;


import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

import javax.json.*;
import javax.validation.constraints.*;

import static eu.ec2u.card.RootServer.*;

import static javax.json.JsonValue.NULL;

@Getter
@Setter
public final class Root {

    private Profile profile;


    public static JsonObject encode(final Root root) {
        return Json.createObjectBuilder()

                .add("profile", Profile.encode(root.profile))

                .build();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Profile {

        @NotNull
        private String esi;

        private User user;
        private Card card;


        public static JsonValue encode(final Profile profile) {
            return profile == null ? NULL : Json.createObjectBuilder()

                    .add("esi", profile.esi)
                    .add("user", User.encode(profile.user))
                    .add("card", Card.encode(profile.card))

                    .build();
        }

        public static Profile decode(final JsonObject json) {
            return json == NULL ? null : new Profile()

                    .setEsi(json.getString("esi"))

                    .setUser(User.decode(Optional.ofNullable(json.get("user"))
                            .filter(JsonObject.class::isInstance)
                            .map(JsonValue::asJsonObject)
                            .orElse(null)
                    ))

                    .setCard(Card.decode(Optional.ofNullable(json.get("card"))
                            .filter(JsonObject.class::isInstance)
                            .map(JsonValue::asJsonObject)
                            .orElse(null)
                    ));
        }

    }

    @Getter
    @Setter
    public abstract static class Container<T extends Resource> extends Resource {

        @NotNull
        @Size(max=ContainerSize)
        private List<T> contains;

    }

    @Getter
    @Setter
    public abstract static class Resource {

        @Size(max=URLSize)
        @Pattern(regexp=RelativePattern)
        private String id;

        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String label;

        @Size(max=TextSize)
        @Pattern(regexp=TextPattern)
        private String brief;

        @Size(max=4096)
        @Pattern(regexp=AbsolutePattern)
        private String image;

    }

}
