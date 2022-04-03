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
import eu.ec2u.card.Setup;
import eu.ec2u.card.services.Codec;
import eu.ec2u.card.services.Fetcher;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;

@Getter
@Setter public final class Root {

    private String version;
    private LocalDateTime instant;

    private Holder holder;
    private List<Card> cards;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Holder {

        private String esi;
        private String uni;

    }

    @Getter
    @Setter
    public static final class Card {

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class Handler implements HttpHandler {

        private static final Pattern BearerPattern=Pattern.compile("\\s*Bearer\\s*(?<token>\\S*)\\s*");


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        @Inject private Setup setup;
        @Inject private Codec codec;
        @Inject private Fetcher fetcher;


        @Override public void handle(final HttpExchange exchange) throws IOException {
            Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"))

                    .map(BearerPattern::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> matcher.group("token"))

                    .map(this::validate)

                    .ifPresentOrElse(

                            holder -> ok(exchange, holder),

                            () -> unauthorized(exchange)

                    );
        }


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        private void ok(final HttpExchange exchange, final Holder holder) {
            try {

                final List<Card> cards=fetcher // !!! error handling
                        .fetch(holder.getEsi())
                        .collect(toList());

                send(exchange, 200, data()
                        .setHolder(holder)
                        .setCards(cards)
                );

            } catch ( final IOException e ) {
                throw new UncheckedIOException(e);
            }
        }

        private void unauthorized(final HttpExchange exchange) {
            try {

                send(exchange, 401, data(),
                        entry("WWW-Authenticate", "Bearer realm=\"card.ec2u.eu\"")
                );

            } catch ( final IOException e ) {
                throw new UncheckedIOException(e);
            }
        }


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        private Holder validate(final String token) {
            return new Holder()
                    .setEsi("urn:schac:personalUniqueCode:int:esi:unipv.it:999001")
                    .setUni("unipv.it");
        }

        private Root data() {
            return new Root()
                    .setVersion(setup.getVersion())
                    .setInstant(setup.getInstant());
        }


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        @SafeVarargs private void send(
                final HttpExchange exchange,
                final int code, final Object body,
                final Map.Entry<String, String>... headers
        ) throws IOException {

            Arrays.stream(headers).forEach(header ->
                    exchange.getResponseHeaders().set(header.getKey(), header.getValue())
            );

            exchange.getResponseHeaders().set("Content-Type", "application/json");

            exchange.sendResponseHeaders(code, 0);

            try (
                    final OutputStream output=exchange.getResponseBody();
                    final Writer writer=new OutputStreamWriter(output, UTF_8)
            ) {

                codec.encode(writer, body);

            }
        }

    }

}
