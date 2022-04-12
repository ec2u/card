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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.handlers.*;

import java.io.IOException;

final class Router implements HttpHandler {

    @Inject private Meta meta;
    @Inject private SSO sso;
    @Inject private ACS acs;

    @Inject private V1 v1;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        final String path=exchange.getRequestURI().getPath();

        switch ( path ) {

            case "/saml":

                meta.handle(exchange);
                break;

            case "/saml/sso":

                sso.handle(exchange);
                break;

            case "/saml/acs":

                acs.handle(exchange);
                break;

            case "/v1/":

                v1.handle(exchange);
                break;

            default:

                exchange.sendResponseHeaders(404, -1);
                break;

        }

    }

}
