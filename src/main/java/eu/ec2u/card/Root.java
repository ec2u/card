/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.ec2u.card;


import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.*;

import static eu.ec2u.card.RootServer.*;

@Getter
@Setter
public final class Root {

    private String id="/";

    private String version;
    private LocalDateTime instant;

    private String gateway;
    private Profile profile;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Profile {

        private String uid; // native user identifier, eg email
        private String esi; // European Student Identifier

        private User user;
        private Card card;

    }

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
        private URI id;

        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String label;

        @Size(max=TextSize)
        @Pattern(regexp=TextPattern)
        private String brief;

        @Size(max=4096)
        private URI image;

    }

}
