/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;
import eu.ec2u.card.ToolSecurity.Profile;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.*;

import static eu.ec2u.card.ToolConfiguration.*;

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

        @Size(max=PathSize)
        @Pattern(regexp=PathPattern)
        @JsonProperty("id")
        private String path;

        @Size(max=LabelSize)
        @Pattern(regexp=LabelPattern)
        private String title;

        @Size(max=NotesSize)
        @Pattern(regexp=NotesPattern)
        private String description;

    }

    public abstract static class ResourceData {

        protected abstract Optional<String> getPath();

        protected abstract Optional<String> getTitle();


        @Id
        protected Long id;

        @Unindexed
        protected String description;


        protected void transfer(final Resource resource, final ResourceData data) {

            data.getPath().ifPresent(resource::setPath);
            data.getTitle().ifPresent(resource::setTitle);

            resource.setDescription(data.description);

        }

        protected static void transfer(final ResourceData data, final Resource resource) {

            if ( !data.getPath().equals(Optional.ofNullable(resource.getPath())) ) {
                throw new IllegalStateException("mutated value for read-only field <id>");
            }

            if ( !data.getTitle().equals(Optional.ofNullable(resource.getTitle())) ) {
                throw new IllegalStateException("mutated value for read-only field <title>");
            }

            data.description=resource.getDescription();

        }

    }

}
