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

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter public final class Profile {

    private Info info;

    private User user;
    private List<Card> cards;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Info {

        private String version;
        private LocalDateTime instant;

    }

    @Getter
    @Setter
    public static final class User {

        private String esi;
        private String hei;

    }

    @Getter
    @Setter
    public static final class Card {

        private String code;
        private String test; // ESC testing service
        private LocalDate expiry;

        private String esi;
        private int level;
        private String name;
        private String photo;

        private HEI hei;

    }

    @Getter
    @Setter
    public static final class HEI {

        private int pic;
        private String name;

        private String iso;
        private String country;

        private String key; // the vault id of the HEI ESC Router API key

    }

}
