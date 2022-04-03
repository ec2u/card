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
@Setter final class CardData {

    private String version;
    private LocalDateTime instant;

    private Holder holder;
    private List<Card> cards;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    static final class Holder {

        private String esi;
        private String uni;

    }

    @Getter
    @Setter
    static final class Card {

        private String code;
        private String test; // ESC testing service
        private LocalDate expiry;

        private String esi;
        private int pic;
        private int level;

        private String name;
        private String photo;
        private String email;

    }

}
