/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import eu.ec2u.card.CardSecurity.Credentials;
import eu.ec2u.card.CardSecurity.Profile;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Service
public class CardService {

    @Autowired private CardConfiguration.Build build;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Card retrieve(final Profile profile) {
        return Card.builder()

                .id(Card.ID)

                .version(build.getVersion())
                .instant(Instant.from(ISO_DATE_TIME.parse(build.getInstant())))

                .profile(profile)

                .build();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Optional<Profile> lookup(@NonNull final Credentials credentials) {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        //return gate.resolve(credentials)
        //
        //		.flatMap(users::findGraphById)
        //
        //		.map(this::profile);
    }


    String login(@NonNull final Profile profile) {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        //return gate.login(update);
    }

    String logout() {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        //return gate.logout();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //private Profile profile(final UserData user) {
    //	return new Profile()
    //
    //			.setAdmin(user.isAdmin())
    //
    //			.setId(user.getId())
    //			.setCode(user.getCode())
    //			.setLabel(user.getLabel())
    //
    //			.setForename(user.getForename())
    //			.setSurname(user.getSurname())
    //			.setEmail(user.getEmail());
    //}

}
