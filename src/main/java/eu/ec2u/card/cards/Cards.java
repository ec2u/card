/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.cards;

import eu.ec2u.card.RootServer;
import eu.ec2u.card.cards.Cards.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

import javax.json.*;
import javax.validation.constraints.*;

import static eu.ec2u.card.Root.Container;
import static eu.ec2u.card.Root.Resource;

import static javax.json.Json.createObjectBuilder;
import static javax.json.Json.createValue;
import static javax.json.JsonValue.NULL;


@Getter
@Setter
public class Cards extends Container<Card> {

    @Getter
    @Setter
    public static final class Card extends Resource {

        @NotNull
        @Size(max=RootServer.URLSize)
        //@Pattern(regexp=ESIPattern) !!! UUID
        private String code;

        @NotNull
        @Size(max=RootServer.URLSize)
        @Pattern(regexp=RootServer.AbsolutePattern)
        private String test;

        @NotNull
        private LocalDate expiry;


        @NotNull
        @Size(max=RootServer.URLSize)
        @Pattern(regexp=RootServer.ESIPattern)
        private String esi;

        @NotNull
        @Size(max=RootServer.URLSize)
        @Min(0) @Max(999999999)
        private int pic;

        @NotNull
        @Size(max=RootServer.URLSize)
        @Min(0) @Max(99)
        private int level;


        @NotNull
        @Size(max=RootServer.LineSize)
        @Pattern(regexp=RootServer.LinePattern)
        private String name;

        @Size(max=RootServer.URLSize)
        @Pattern(regexp=RootServer.AbsolutePattern)
        private String photo;

        @Size(max=RootServer.LineSize)
        @Email
        private String email;


        public static JsonValue encode(final Card card) {
            return card == null ? NULL : createObjectBuilder()

                    .add("code", createValue(card.code))
                    .add("test", createValue(card.test))
                    .add("expiry", createValue(card.expiry.toString()))

                    .add("esi", createValue(card.esi))
                    .add("pic", createValue(card.pic))
                    .add("level", createValue(card.level))

                    .add("name", createValue(card.name))

                    .add("photo", Optional.ofNullable(card.photo)
                            .map(Json::createValue)
                            .map(JsonValue.class::cast)
                            .orElse(NULL))

                    .add("email", Optional.ofNullable(card.email)
                            .map(Json::createValue)
                            .map(JsonValue.class::cast)
                            .orElse(NULL))

                    .build();
        }


        public static Card decode(final JsonObject json) {
            return null; // !!!
        }

    }

}


