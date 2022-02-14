/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.fasterxml.jackson.annotation.JsonProperty;
import eu.ec2u.card.ToolSecurity.Profile;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static eu.ec2u.card.ToolConfiguration.ContainerSize;

@Getter
@Builder
public final class Tool {

    public static final String Id="/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String id;

    private final Instant revision;
    private final Profile profile;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        @JsonProperty(access=READ_ONLY)
        private String id;

        @JsonProperty(access=READ_ONLY)
        private String label;

    }

    @Getter
    public abstract static class ResourceData {

        @Id
        protected Long id;

    }

}
