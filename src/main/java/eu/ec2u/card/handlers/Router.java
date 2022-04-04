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
import eu.ec2u.card.Setup;
import eu.ec2u.card.services.Codec;
import eu.ec2u.card.services.Fetcher;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Router implements HttpHandler {

    private static final Pattern BearerPattern=Pattern.compile("\\s*Bearer\\s*(?<token>\\S*)\\s*");


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private Setup setup;
    @Inject private Codec codec;
    @Inject private Fetcher fetcher;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        if ( !exchange.getRequestURI().getPath().equals("/") ) {

            exchange.sendResponseHeaders(404, -1);

        } else if ( !exchange.getRequestMethod().equals("GET") ) {

            exchange.sendResponseHeaders(405, -1);

        } else {

            final Profile data=new Profile()
                    .setManager(setup.getManager())
                    .setVersion(setup.getVersion())
                    .setInstant(setup.getInstant());

            final int status=Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"))

                    .map(BearerPattern::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> matcher.group("token"))

                    .map(this::validate) // !!! malformed/expired

                    .map(user -> {

                        final List<Profile.Card> cards=fetcher.fetch(user);// !!! error handling

                        data
                                .setUser(user)
                                .setCards(cards);

                        return 200;

                    })

                    .orElseGet(() -> {

                        exchange.getResponseHeaders().set(
                                "WWW-Authenticate", format("Bearer realm=\"%s\"", setup.getSso())
                        );

                        return 401;

                    });


            exchange.getResponseHeaders().set("Content-Type", "application/json");

            exchange.sendResponseHeaders(status, 0);

            try (
                    final OutputStream output=exchange.getResponseBody();
                    final Writer writer=new OutputStreamWriter(output, UTF_8)
            ) {

                codec.encode(writer, data);

            }

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Profile.User validate(final String token) { // !!!
        return new Profile.User()
                .setEsi("urn:schac:personalUniqueCode:int:esi:unipv.it:999001")
                .setHei("unipv.it");
    }

}
