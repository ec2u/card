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

import eu.ec2u.card.Profile.HEI;
import eu.ec2u.card.services.Fetcher.ESC;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public final class Setup {

    private String version;
    private LocalDateTime instant;

    private ESC esc;

    private Map<String, HEI> tenants;

}
