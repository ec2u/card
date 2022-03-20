/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.cards;

import com.metreeca.rest.Xtream;
import com.metreeca.rest.actions.*;

import eu.ec2u.card.cards.Cards.Card;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonValue;
import javax.validation.constraints.*;

import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.formats.JSONFormat.json;
import static com.metreeca.rest.services.Vault.vault;

import static eu.ec2u.card.Root.*;

import static javax.json.JsonValue.NULL;


@Getter
@Setter
public class Cards extends Container<Card> {

    static final String Id="/cards/";

    private static final String API="api-sandbox";
    private static final String KEY="key-esc-router-sandbox";


    public static Stream<Card> esc(final String esi) {

        if ( esi == null ) {
            throw new NullPointerException("null esi");
        }

        return Xtream.of(esi)

                .flatMap(new Fill<>()

                        .model("https://{api}.europeanstudentcard.eu/v1/students/{esi}")

                        .value("api", API)
                        .value("esi")

                )

                .optMap(new GET<>(json(), request -> request.header("Key", service(vault())
                        .get(KEY)
                        .orElseThrow() // !!!
                )))

                .flatMap(new JSONPath<>(json -> json.strings("cards.*.europeanStudentCardNumber").map(code -> new Card() // !!! error handling

                        .setCode(code)
                        .setExpiry(json.string("expiryDate")
                                .map(ZonedDateTime::parse)
                                .map(ZonedDateTime::toLocalDate)
                                .orElseThrow()
                        )

                        .setEsi(json.string("europeanStudentIdentifier").orElseThrow())
                        .setPic(json.integer("picInstitutionCode").map(BigInteger::intValue).orElseThrow())
                        .setLevel(json.integer("academicLevel").map(BigInteger::intValue).orElseThrow())

                        .setName(json.string("name").orElseThrow())
                        .setPhoto(json.string("photoID").orElse(null)) // !!!
                        .setEmail(json.string("emailAddress").orElse(null)))
                ));
    }


    public static JsonValue encode(final Card card) {
        return card == null ? NULL : Json.createObjectBuilder()

                .add("code", Json.createValue(card.code))
                .add("expiry", Json.createValue(card.expiry.toString()))

                .add("esi", Json.createValue(card.esi))
                .add("pic", Json.createValue(card.pic))
                .add("level", Json.createValue(card.level))

                .add("name", Json.createValue(card.name))

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

    @Getter
    @Setter
    public static final class Card extends Resource {

        @NotNull
        @Size(max=URLSize)
        //@Pattern(regexp=ESIPattern) !!! UUID
        private String code;

        @NotNull
        private LocalDate expiry;


        @NotNull
        @Size(max=URLSize)
        @Pattern(regexp=ESIPattern)
        private String esi;

        @NotNull
        @Size(max=URLSize)
        @Min(0) @Max(999999999)
        private int pic;

        @NotNull
        @Size(max=URLSize)
        @Min(0) @Max(99)
        private int level;


        @NotNull
        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String name;

        @Size(max=URLSize)
        @Pattern(regexp=AbsolutePattern)
        private String photo;

        @Size(max=LineSize)
        @Email
        private String email;

    }

}


