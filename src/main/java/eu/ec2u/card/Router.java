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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpExchange;
import eu.ec2u.card.handlers.Root;

import java.io.IOException;
import java.util.Map;

import static java.util.Map.entry;

public final class Router extends Handler {

    private static final Handler Unknown=new Handler() {

        @Override public void handle(final HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(NotFound, -1);
        }

    };


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Map<String, Handler> handlers=Map.ofEntries(
            entry("/", new Root.Route())
    );


    @Inject public Router(final Injector injector) {
        handlers.forEach((path, handler) -> injector.injectMembers(handler));
    }


    @Override public void handle(final HttpExchange exchange) throws IOException {
        handlers.getOrDefault(exchange.getRequestURI().getPath(), Unknown).handle(exchange);
    }

}
