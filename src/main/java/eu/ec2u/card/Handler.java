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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.handlers.Root.Holder;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Handler implements HttpHandler {

    public static final int OK=200;
    public static final int Unauthorized=401;
    public static final int NotFound=404;

    private static final Pattern HTMLPattern=Pattern.compile("\\btext/x?html\\b");
    private static final Pattern FilePattern=Pattern.compile("\\.\\w+$");


    public static boolean isSafe(final HttpExchange exchange) {
        return exchange.getRequestMethod().equalsIgnoreCase("GET");
    }

    public static boolean isAsset(final HttpExchange exchange) {
        return Optional.ofNullable(exchange.getRequestURI().getPath())
                .map(FilePattern::matcher)
                .filter(Matcher::find)
                .isPresent();
    }

    public static boolean isInteractive(final HttpExchange exchange) {
        return Optional.ofNullable(exchange.getRequestHeaders().getFirst("Accept"))
                .map(HTMLPattern::matcher)
                .filter(Matcher::find)
                .isPresent();
    }


    public static Optional<Holder> holder(final HttpExchange exchange) {
        return Optional.of(Holder.class.getName())

                .map(exchange::getAttribute)
                .filter(Holder.class::isInstance)
                .map(Holder.class::cast);
    }

    public static HttpExchange holder(final HttpExchange exchange, final Holder holder) {

        exchange.setAttribute(Holder.class.getName(), holder);

        return exchange;
    }

}
