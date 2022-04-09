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
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.Setup.HEI;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * HTTP resource handler.
 */
final class Handler implements HttpHandler {

    @Inject private Setup setup;
    @Inject private Gson codec;
    @Inject private Fetcher fetcher;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        if ( !exchange.getRequestURI().getPath().equals("/") ) {

            exchange.sendResponseHeaders(404, -1);

        } else if ( !exchange.getRequestMethod().equals("GET") ) {

            exchange.sendResponseHeaders(405, -1);

        } else {

            final Profile data=new Profile()
                    .setVersion(setup.getVersion())
                    .setInstant(setup.getInstant());

            final int status=Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"))

                    .map(token -> new User() // !!! from request parameters
                            .setEsi("urn:schac:personalUniqueCode:int:esi:unipv.it:999001")
                            .setHei("unipv.it")
                    )

                    .map(user -> {

                        final List<Card> cards=fetcher.fetch(user); // !!! error handling

                        data
                                .setUser(user)
                                .setCards(cards);

                        return 200;

                    })

                    .orElseGet(() -> {

                        //exchange.getResponseHeaders().set(
                        //        "WWW-Authenticate", format("Bearer realm=\"%s\"", setup.getSso())
                        //);

                        return 401;

                    });


            exchange.getResponseHeaders().set("Content-Type", "application/json");

            exchange.sendResponseHeaders(status, 0);

            try (
                    final OutputStream output=exchange.getResponseBody();
                    final Writer writer=new OutputStreamWriter(output, UTF_8)
            ) {

                codec.toJson(data, writer);

            }

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter private static final class Profile {

        private String version;
        private LocalDateTime instant;

        private User user;
        private List<Card> cards;

    }

    @Getter
    @Setter
    static final class User {

        private String esi;
        private String hei;

    }

    @Getter
    @Setter
    static final class Card {

        private String code;
        private String test; // ESC testing service
        private LocalDate expiry;

        private String esi;
        private int level;
        private String name;
        private String photo;

        private HEI hei;

    }

}
