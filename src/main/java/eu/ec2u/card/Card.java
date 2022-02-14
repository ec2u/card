/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.fasterxml.jackson.annotation.JsonProperty;
import eu.ec2u.card.CardSecurity.Profile;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static eu.ec2u.card.CardConfiguration.*;

@Getter
@Builder
public final class Card {

    public static final String Path="/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String id;

    private final Instant revision;
    private final Profile profile;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @SuperBuilder
    public abstract static class Resource {

        @NonNull
        @Size(max=IdSizeLimit)
        @Pattern(regexp="/(\\w+/)*\\w+/?")
        @JsonProperty(access=READ_ONLY)
        private final String id;

        @Size(min=2, max=LabelSizeLimit)
        @Pattern(regexp="\\S+( \\S+)*")
        @JsonProperty(access=READ_ONLY)
        private final String label;

    }

    @Getter
    @SuperBuilder
    public abstract static class Container<T extends Resource> extends Resource {

        @NonNull
        @Size(max=ContainerSizeLimit)
        private final List<T> contains;

    }

}
