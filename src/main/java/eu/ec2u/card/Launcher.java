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

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.net.http.HttpClient.newHttpClient;
import static java.util.Map.entry;

public final class Launcher extends Filter {

    private static final Map<String, String> Mimes=Map.ofEntries(
            entry("html", "text/html"),
            entry("css", "text/css"),
            entry("js", "text/javascript"),
            entry("svg", "image/svg+xml"),
            entry("png", "image/png"),
            entry("webp", "image/webp")
    );


    private static final Pattern HTMLPattern=Pattern.compile("\\btext/x?html\\b");
    private static final Pattern FilePattern=Pattern.compile("\\.\\w+$");


    private static boolean isSafe(final HttpExchange exchange) {
        return exchange.getRequestMethod().equalsIgnoreCase("GET");
    }

    private static boolean isAsset(final HttpExchange exchange) {
        return Optional.ofNullable(exchange.getRequestURI().getPath())
                .map(FilePattern::matcher)
                .filter(Matcher::find)
                .isPresent();
    }

    private static boolean isInteractive(final HttpExchange exchange) {
        return Optional.ofNullable(exchange.getRequestHeaders().getFirst("Accept"))
                .map(HTMLPattern::matcher)
                .filter(Matcher::find)
                .isPresent();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override public String description() {
        return "SPA loader";
    }

    @Override public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {

        if ( isSafe(exchange) ) {

            if ( isAsset(exchange) ) {

                asset(exchange);

            } else if ( isInteractive(exchange) ) {

                fallback(exchange);

            } else {

                chain.doFilter(exchange);

            }

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void asset(final HttpExchange exchange) throws IOException {

        final String path=exchange.getRequestURI().normalize().getPath(); // normalize to prevent tree walking
        final String type=path.substring(path.lastIndexOf('.')+1);

        final URL asset=Launcher.class.getResource(format("/static%s", path));

        if ( asset == null ) {

            exchange.sendResponseHeaders(404, -1);

        } else {

            exchange.getResponseHeaders().set("Content-Type",
                    Mimes.getOrDefault(type, "application/octet-stream")
            );

            exchange.sendResponseHeaders(200, 0);

            try (
                    final InputStream input=asset.openStream();
                    final OutputStream output=exchange.getResponseBody()
            ) {

                input.transferTo(output);

            }

        }
    }

    private void fallback(final HttpExchange exchange) throws IOException {

        final Headers headers=exchange.getRequestHeaders();

        final String proto=Optional.ofNullable(headers.getFirst("X-Forwarded-Proto"))
                .or(() -> Optional.ofNullable(exchange.getProtocol())
                        .map(p -> p.substring(0, p.indexOf('/')))
                        .map(p -> p.toLowerCase(Locale.ROOT))
                )
                .orElse("http");

        final String host=Optional.ofNullable(headers.getFirst("X-Forwarded-Host"))
                .or(() -> Optional.ofNullable(headers.getFirst("Host")))
                .orElse("localhost");


        final String index=format("%s://%s/index.html", proto, host);

        final HttpRequest request=HttpRequest.newBuilder().GET()
                .uri(URI.create(index))
                .build();

        // !!! prevent loops

        try {

            final HttpResponse<InputStream> response=newHttpClient().send(request, BodyHandlers.ofInputStream());

            // !!! network error handling

            exchange.getResponseHeaders().set("Content-Type", response.headers()
                    .firstValue("Content-Type")
                    .orElse("text/html")
            );


            exchange.sendResponseHeaders(200, 0);

            try (
                    final InputStream input=response.body();
                    final OutputStream output=exchange.getResponseBody()
            ) {

                input.transferTo(output);

            }

        } catch ( final InterruptedException e ) {
            throw new RuntimeException(e); // !!! handle
        }

    }

}
