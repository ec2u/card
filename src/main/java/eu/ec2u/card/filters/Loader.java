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

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Locale;
import java.util.Optional;

import static eu.ec2u.card.Handler.*;

import static java.lang.String.format;
import static java.net.http.HttpClient.newHttpClient;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Loader extends Filter {

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

        // !!! prevent tree walking

        final URL asset=Loader.class.getResource("/static"+exchange.getRequestURI());


        if ( asset == null ) {

            exchange.sendResponseHeaders(NotFound, -1);

        } else {

            exchange.sendResponseHeaders(OK, 0);
            //exchange.getResponseHeaders().set("Content-Type", "text/html");

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
                        .map(p -> p.substring(p.indexOf('/')))
                        .map(p -> p.toLowerCase(Locale.ROOT))
                )
                .orElse("http");

        final String host=Optional.ofNullable(headers.getFirst("X-Forwarded-Host"))
                .or(() -> Optional.ofNullable(headers.getFirst("Host")))
                .orElse("localhost");


        final String index=format("%s://%s/", proto, host);

        final HttpRequest request=HttpRequest.newBuilder().GET()
                .uri(URI.create(index))
                .build();

        // !!! prevent loops

        try {

            final HttpResponse<String> response=newHttpClient().send(request, BodyHandlers.ofString());

            // !!! network error handling

            exchange.getResponseHeaders().set("Content-Type", "text/html");

            exchange.sendResponseHeaders(OK, 0);

            try (
                    final OutputStream output=exchange.getResponseBody();
            ) {

                output.write(response.body().getBytes(UTF_8));

            }

        } catch ( final InterruptedException e ) {
            throw new RuntimeException(e); // !!! handle
        }

    }

}
