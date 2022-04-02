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

package eu.ec2u.card.users;

import eu.ec2u.card.RootServer;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

import static eu.ec2u.card.Root.Container;
import static eu.ec2u.card.Root.Resource;

@Getter
@Setter
public class Users extends Container<User> {

    @Getter
    @Setter
    public static final class User extends Resource {

        @NotNull
        @Size(max=RootServer.URLSize)
        @Pattern(regexp=RootServer.ESIPattern)
        private String esi;

        @NotNull
        private Boolean admin;


        @NotNull
        @Size(max=RootServer.LineSize)
        @Pattern(regexp=RootServer.LinePattern)
        private String forename;

        @NotNull
        @Size(max=RootServer.LineSize)
        @Pattern(regexp=RootServer.LinePattern)
        private String surname;


        @NotNull
        @Size(max=RootServer.LineSize)
        @Email
        private String email;

    }

}
