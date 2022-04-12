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

package eu.ec2u.card.handlers;

import com.google.inject.Inject;
import com.onelogin.saml2.settings.Saml2Settings;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.services.Settings;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Meta implements HttpHandler {

    @Inject private Settings settings;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        if ( exchange.getRequestMethod().equals("GET") ) {

            final Saml2Settings settings=this.settings.get();

            settings.setSPValidationOnly(true);

            //List<String> errors = settings.checkSettings();
            //
            //if (errors.isEmpty()) {
            //    String metadata = settings.getSPMetadata();
            //    out.println(metadata);
            //} else {
            //    response.setContentType("text/html; charset=UTF-8");
            //
            //    for (String error : errors) {
            //        out.println("<p>"+error+"</p>");
            //    }
            //}

            exchange.getResponseHeaders().set("Content-Type", "application/xml");

            exchange.sendResponseHeaders(200, 0);

            try ( final OutputStream output=exchange.getResponseBody() ) {

                output.write(settings.getSPMetadata().getBytes(UTF_8));

            } catch ( final CertificateEncodingException e ) {
                throw new RuntimeException(e); // !!!
            }

        } else {

            exchange.sendResponseHeaders(405, -1);
        }

    }

}
