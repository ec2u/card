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

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;

public final class Work {

    private static final Pattern HostPattern=Pattern.compile("\\bhost\\s*=\\s*(?<host>[^;]+)");
    private static final Pattern ProtoPattern=Pattern.compile("\\bproto\\s*=\\s*(?<proto>[^;]+)");


    public static String base(final HttpExchange exchange) { // reconstruct public base from proxy forward headers

        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Forwarded

        final Optional<String> forwarded=Optional.ofNullable(exchange.getResponseHeaders().getFirst("Forwarded"));

        final String proto=forwarded

                .map(ProtoPattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group("proto"))

                .or(() -> exchange.getRequestHeaders().entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase("X-Forwarded-Proto"))
                        .flatMap(entry -> entry.getValue().stream())
                        .findFirst()
                )

                .orElse("http");

        final String host=forwarded

                .map(HostPattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group("host"))

                .or(() -> exchange.getRequestHeaders().entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase("X-Forwarded-Host"))
                        .flatMap(entry -> entry.getValue().stream())
                        .findFirst()
                )

                .orElse("localhost");

        return format("%s://%s%s", proto, host, exchange.getRequestURI().getPath());
    }


    /**
     * Converts a parameter map into a query string.
     *
     * @param parameters the parameter map to be converted
     *
     * @return a query string built from {@code parameters}
     *
     * @throws NullPointerException if {@code parameters} is null or contains null values
     */
    public static String query(final Map<String, List<String>> parameters) {

        if ( parameters == null || parameters.entrySet().stream().anyMatch(entry ->
                entry.getKey() == null || entry.getValue() == null || entry.getValue().stream().anyMatch(Objects::isNull)
        ) ) {
            throw new NullPointerException("null parameters");
        }

        return parameters.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(value -> entry(entry.getKey(), value)))
                .map(p -> format("%s=%s", URLEncoder.encode(p.getKey(), UTF_8), URLEncoder.encode(p.getValue(), UTF_8)))
                .collect(joining("&"));
    }

    /**
     * Converts a query string into a parameter map.
     *
     * @param query the query string to be converted
     *
     * @return a parameter map parsed from {@code query}
     *
     * @throws NullPointerException if {@code query} is null
     */
    public static Map<String, List<String>> parameters(final String query) {

        if ( query == null ) {
            throw new NullPointerException("null query");
        }

        final Map<String, List<String>> parameters=new LinkedHashMap<>();

        final int length=query.length();

        for (int head=0, tail; head < length; head=tail+1) {

            final int equal=query.indexOf('=', head);
            final int ampersand=query.indexOf('&', head);

            tail=(ampersand >= 0) ? ampersand : length;

            final boolean split=equal >= 0 && equal < tail;

            final String label=URLDecoder.decode(query.substring(head, split ? equal : tail), UTF_8);
            final String value=URLDecoder.decode(query.substring(split ? equal+1 : tail, tail), UTF_8);

            parameters.compute(label, (name, values) -> {

                final List<String> strings=(values != null) ? values : new ArrayList<>();

                strings.add(value);

                return strings;

            });

        }

        return parameters;
    }

}
