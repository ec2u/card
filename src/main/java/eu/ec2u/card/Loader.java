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

import com.google.inject.*;
import eu.ec2u.card.services.*;

import java.io.*;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Objects;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Shared service loader.
 */
final class Loader extends AbstractModule {

    @Provides @Inject Setup setup(final Codec codec) throws IOException, ParseException {

        final String setup=format("%s.json", Setup.class.getSimpleName());

        try (
                final InputStream input=Objects.requireNonNull(getClass().getResourceAsStream(setup));
                final Reader reader=new InputStreamReader(input, UTF_8)
        ) {

            return codec.decode(reader, Setup.class);  // !!! verify all values

        }

    }

    @Provides @Inject public Notary notary(final Vault vault) {
        return new Notary(vault.get("jwt-key")
                .map(key -> key.getBytes(UTF_8))
                .orElseThrow(() -> new NoSuchElementException(format("undefined <%s> key", "key-jwt")))
        );
    }

}
