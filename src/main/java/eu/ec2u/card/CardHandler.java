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

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import eu.ec2u.card.CardData.Holder;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;

final class CardHandler {

    private static final Pattern BearerPattern=Pattern.compile("\\s*Bearer\\s*(?<token>\\S*)\\s*");


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private CardSetup setup;
    @Inject private CardFetcher fetcher;

    @Inject private Gson gson;


    void handle(final HttpExchange exchange) throws IOException {
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

            final List<CardData.Card> cards=fetcher // !!! error handling
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

    private CardData data() {
        return new CardData()
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

            gson.toJson(body, writer);

        }
    }

}
