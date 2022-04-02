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

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static com.metreeca.core.Lambdas.checked;
import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.Toolbox.text;

import static work.BeanCodec.codec;

@Getter
public final class RootSetup {

    public static Supplier<RootSetup> setup() {
        return checked(() -> service(codec()).decode(text(RootSetup.class, ".json"), RootSetup.class));
    }


    private String version;
    private LocalDateTime instant;

    private String gateway;

}
