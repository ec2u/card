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

package eu.ec2u.card.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.Profile;
import eu.ec2u.card.Profile.Info;
import eu.ec2u.card.Setup;
import eu.ec2u.card.services.*;

import java.io.*;
import java.text.ParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class V1 implements HttpHandler {

    private static final Pattern BearerPattern=Pattern.compile("\\s*Bearer\\s*(?<token>\\S*)\\s*");


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private Setup setup;
    @Inject private Codec codec;
    @Inject private Notary notary;
    @Inject private Fetcher fetcher;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        if ( exchange.getRequestMethod().equals("GET") ) {

            final Profile profile=new Profile()

                    .setInfo(new Info()
                            .setVersion(setup.getVersion())
                            .setInstant(setup.getInstant())
                    );

            final int status=Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"))

                    .map(BearerPattern::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> matcher.group("token"))

                    .flatMap(token -> notary.verify(token))

                    .map(token -> {

                        try {

                            return codec.decode(new StringReader(token), Profile.User.class);

                        } catch ( final IOException e ) {

                            throw new UncheckedIOException(e);

                        } catch ( final ParseException e ) {

                            return null;
                        }

                    })

                    .map(user -> {

                        profile
                                .setUser(user)
                                .setCards(fetcher.fetch(user));  // !!! error handling

                        return 200;

                    })

                    .orElseGet(() -> {

                        exchange.getResponseHeaders().set(
                                "WWW-Authenticate", "Bearer realm=\"card.ec2u.eu"+"\""
                        );

                        return 401;

                    });


            exchange.getResponseHeaders().set("Content-Type", "application/json");

            exchange.sendResponseHeaders(status, 0);

            try (
                    final OutputStream output=exchange.getResponseBody();
                    final Writer writer=new OutputStreamWriter(output, UTF_8)
            ) {

                codec.encode(writer, profile);

            }


        } else {

            exchange.sendResponseHeaders(405, -1);

        }

    }

}
