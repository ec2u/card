/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;
import eu.ec2u.card.CardSecurity.Profile;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.*;

import static eu.ec2u.card.CardConfiguration.*;

@Getter
@Builder
public final class Card {

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

        @Size(max=URLSize)
        @Pattern(regexp=RelativePattern)
        @JsonProperty("id")
        private String path;

        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String label;

        @Size(max=LineSize)
        @Pattern(regexp=AbsolutePattern)
        private String image;

        @Size(max=TextSize)
        @Pattern(regexp=TextPattern)
        private String brief;

    }

    public abstract static class ResourceData {

        protected abstract Optional<String> getPath();

        protected abstract Optional<String> getLabel();


        @Id
        protected Long id;

        @Unindexed
        protected String image;

        @Unindexed
        protected String brief;


        protected void transfer(final Resource resource, final ResourceData data) {

            data.getPath().ifPresent(resource::setPath);
            data.getLabel().ifPresent(resource::setLabel);

            resource.setImage(data.image);
            resource.setBrief(data.brief);

        }

        protected static void transfer(final ResourceData data, final Resource resource) {

            if ( !data.getPath().equals(Optional.ofNullable(resource.getPath())) ) {
                throw new IllegalStateException("mutated value for read-only field <id>");
            }

            if ( !data.getLabel().equals(Optional.ofNullable(resource.getLabel())) ) {
                throw new IllegalStateException("mutated value for read-only field <brief>");
            }

            data.image=resource.getImage();
            data.brief=resource.getBrief();

        }

    }

}
