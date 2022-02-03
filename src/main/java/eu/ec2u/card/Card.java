/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import eu.ec2u.card.CardSecurity.Profile;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public final class Card {

    public static final String ID="/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String id;

    private final String version;
    private final Instant instant;

    private final Profile profile;

}
