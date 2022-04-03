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

final class Services extends AbstractModule {

    @Provides @Inject Setup setup(final Codec codec) throws IOException, ParseException {

        if ( codec == null ) {
            throw new NullPointerException("null codec");
        }

        final String setup="application.json";
        final ClassLoader loader=getClass().getClassLoader();

        try (
                final InputStream input=Objects.requireNonNull(loader.getResourceAsStream(setup));
                final Reader reader=new InputStreamReader(input, UTF_8)
        ) {

            return codec.decode(reader, Setup.class);

        }

    }

    @Provides @Inject Notary notary(final Setup setup, final Vault vault) {
        return new Notary(vault.get(setup.getJwt())
                .map(key -> key.getBytes(UTF_8))
                .orElseThrow(() -> new NoSuchElementException(format("undefined <%s> key", setup.getJwt())))
        );
    }

}
