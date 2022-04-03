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

package eu.ec2u.card.filters;

import com.google.inject.Inject;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import eu.ec2u.card.Setup;
import eu.ec2u.card.handlers.Root.Holder;
import eu.ec2u.card.services.Notary;
import lombok.experimental.Delegate;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.ec2u.card.Handler.Unauthorized;
import static eu.ec2u.card.Handler.holder;

import static java.lang.String.format;

public final class Keeper extends Filter {

    private static final Pattern BearerPattern=Pattern.compile("\\s*Bearer\\s*(?<token>\\S*)\\s*");


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private Setup setup;
    @Inject private Notary notary;


    @Override public String description() {
        return "bearer token authenticator";
    }

    @Override public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {

        Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"))

                .map(BearerPattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("token"))

                .map(this::validate) // !!! malformed/expired

                .ifPresent(holder -> holder(exchange, holder));

        chain.doFilter(new ExchangeWrapper(exchange) {

            @Override public void sendResponseHeaders(final int rCode, final long responseLength) throws IOException {

                if ( rCode == Unauthorized ) {
                    getResponseHeaders().set("WWW-Authenticate", format("Bearer realm=\"%s\"", setup.getSso()));
                }

                super.sendResponseHeaders(rCode, responseLength);
            }

        });

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Holder validate(final String token) {
        return new Holder()
                .setEsi("urn:schac:personalUniqueCode:int:esi:unipv.it:999001")
                .setUni("unipv.it");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private abstract static class ExchangeWrapper extends HttpExchange {

        @Delegate private final HttpExchange exchange;

        ExchangeWrapper(final HttpExchange exchange) { this.exchange=exchange; }

    }

}
