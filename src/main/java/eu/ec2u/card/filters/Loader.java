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

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static eu.ec2u.card.Handler.*;

import static java.lang.String.format;

public final class Loader extends Filter {

    @Override public String description() {
        return "SPA loader";
    }

    @Override public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {

        if ( isSafe(exchange) && isInteractive(exchange) && !isAsset(exchange) ) {

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


            final String index=format("%s://%s/index.html", proto, host);

            // !!! replace

            exchange.getResponseHeaders().set("Location", index);
            exchange.sendResponseHeaders(302, -1);

            //final HttpRequest request=HttpRequest.newBuilder().GET()
            //        .uri(URI.create(index))
            //        .build();
            //
            //// !!! prevent loops
            //
            //try {
            //
            //    final HttpResponse<String> response=newHttpClient().send(request, BodyHandlers.ofString());
            //
            //    // !!! network error handling
            //
            //    exchange.getResponseHeaders().set("Content-Type", "text/html");
            //
            //    exchange.sendResponseHeaders(OK, 0);
            //
            //    try (
            //            final OutputStream output=exchange.getResponseBody();
            //    ) {
            //
            //        output.write(response.body().getBytes(UTF_8));
            //
            //    }
            //
            //} catch ( final InterruptedException e ) {
            //    throw new RuntimeException(e); // !!! handle
            //}

        } else {

            chain.doFilter(exchange);

        }

    }

}
