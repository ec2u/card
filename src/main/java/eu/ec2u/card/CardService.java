/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import eu.ec2u.card.CardSecurity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired private CardConfiguration properties;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Card retrieve(final Profile profile) {
        return Card.builder()

                .id(Card.Id)

                .revision(properties.revision())
                .profile(profile)

                .build();
    }

}
