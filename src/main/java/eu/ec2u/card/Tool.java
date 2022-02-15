/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;
import eu.ec2u.card.ToolSecurity.Profile;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

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

        @Size(max=IdSize)
        @Pattern(regexp=IdPattern)
        private String id;

        @Size(max=LabelSize)
        @Pattern(regexp=LabelPattern)
        private String title;

        @Size(max=NotesSize)
        @Pattern(regexp=NotesPattern)
        private String description;

    }

    public abstract static class ResourceData {

        @Id
        protected Long id;

        @Unindexed
        protected String description;

    }

}
