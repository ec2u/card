/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import eu.ec2u.card.CardSecurity.Credentials;
import eu.ec2u.card.CardSecurity.Profile;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Optional;

import static java.time.temporal.ChronoField.*;

@Service
public class CardService {

    private static final DateTimeFormatter InstantFormat=new DateTimeFormatterBuilder()

            .appendValue(YEAR, 4)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)

            .appendLiteral("T")

            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)

            .parseStrict()
            .toFormatter(Locale.ROOT);

    @Autowired
    private CardProperties properties;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Card retrieve(final Profile profile) {
        return Card.builder()

                .id(Card.ID)

                .revision(
                        LocalDateTime.from(InstantFormat.parse(properties.getRevision())).toInstant(ZoneOffset.UTC))

                .profile(profile)

                .build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Optional<Profile> lookup(@NonNull final Credentials credentials) {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        // return gate.resolve(credentials)
        //
        // .flatMap(users::findGraphById)
        //
        // .map(this::profile);
    }

    String login(@NonNull final Profile profile) {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        // return gate.login(update);
    }

    String logout() {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        // return gate.logout();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // private Profile profile(final UserData user) {
    // return new Profile()
    //
    // .setAdmin(user.isAdmin())
    //
    // .setId(user.getId())
    // .setCode(user.getCode())
    // .setLabel(user.getLabel())
    //
    // .setForename(user.getForename())
    // .setSurname(user.getSurname())
    // .setEmail(user.getEmail());
    // }

}
