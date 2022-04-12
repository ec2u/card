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
import com.onelogin.saml2.authn.SamlResponse;
import com.onelogin.saml2.factory.SamlMessageFactory;
import com.onelogin.saml2.http.HttpRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.Profile;
import eu.ec2u.card.services.*;

import java.io.*;
import java.util.List;
import java.util.Map;

import static eu.ec2u.card.Work.base;
import static eu.ec2u.card.Work.parameters;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class ACS implements HttpHandler {

    private static final SamlMessageFactory samlMessageFactory=new SamlMessageFactory() { };


    @Inject private Settings settings;
    @Inject private Notary notary;
    @Inject private Codec codec;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        if ( exchange.getRequestMethod().equals("POST") ) {

            try {

                final InputStream input=exchange.getRequestBody();

                final String query=new String(input.readAllBytes(), UTF_8);

                final Map<String, List<String>> parameters=parameters(query);

                final HttpRequest http=new HttpRequest(
                        base(exchange),
                        parameters,
                        query
                );

                final SamlResponse samlResponse=samlMessageFactory.createSamlResponse(settings.get(), http);

                //lastResponse = samlResponse.getSAMLResponseXml();

                if ( samlResponse.isValid(null) ) {

                    // !!! replay attack?

                    //nameid = samlResponse.getNameId();
                    //nameidFormat = samlResponse.getNameIdFormat();
                    //nameidNameQualifier = samlResponse.getNameIdNameQualifier();
                    //nameidSPNameQualifier = samlResponse.getNameIdSPNameQualifier();
                    //authenticated = true;
                    //attributes = samlResponse.getAttributes();
                    //sessionIndex = samlResponse.getSessionIndex();
                    //sessionExpiration = samlResponse.getSessionNotOnOrAfter();

                    // !!! replay attack?

                    //lastMessageId = samlResponse.getId();
                    //lastMessageIssueInstant = samlResponse.getResponseIssueInstant();
                    //lastAssertionId = samlResponse.getAssertionId();
                    //lastAssertionNotOnOrAfter = samlResponse.getAssertionNotOnOrAfter();

                    //LOGGER.debug("processResponse success --> " + samlResponseParameter);

                    final String esi=samlResponse.getAttributes()
                            .getOrDefault("schacPersonalUniqueCode", List.of())
                            .stream()
                            .findFirst()
                            .orElseThrow();

                    final String token=notary.create(codec.encode(new StringWriter(), new Profile.User()
                            .setEsi(esi)
                            .setHei("unipv.it") // !!!
                    ).toString());

                    final String target=parameters
                            .getOrDefault("RelayState", List.of())
                            .stream()
                            .findFirst()
                            .orElse("/");

                    exchange.getResponseHeaders().set("Location", format("%s?%s", target, token));

                    exchange.sendResponseHeaders(302, -1);

                } else {

                    //errorReason = samlResponse.getError();
                    //validationException = samlResponse.getValidationException();
                    //final SamlResponseStatus samlResponseStatus = samlResponse.getResponseStatus();
                    //if (samlResponseStatus.getStatusCode() == null || !samlResponseStatus.getStatusCode().equals
                    // (Constants.STATUS_SUCCESS)) {
                    //    errors.add("response_not_success");
                    //    LOGGER.error("processResponse error. sso_not_success");
                    //    LOGGER.debug(" --> " + samlResponseParameter);
                    //    errors.add(samlResponseStatus.getStatusCode());
                    //    if (samlResponseStatus.getSubStatusCode() != null) {
                    //        errors.add(samlResponseStatus.getSubStatusCode());
                    //    }
                    //} else {
                    //    errors.add("invalid_response");
                    //    LOGGER.error("processResponse error. invalid_response");
                    //    LOGGER.debug(" --> " + samlResponseParameter);
                    //}

                    //errors.add("invalid_binding");
                    //final String errorMsg = "SAML Response not found, Only supported HTTP_POST Binding";
                    //LOGGER.error("processResponse error." + errorMsg);
                    //throw new Error(errorMsg, Error.SAML_RESPONSE_NOT_FOUND);

                    exchange.sendResponseHeaders(400, -1);

                }

            } catch ( final Exception e ) {

                throw new RuntimeException(e); // !!!
            }


        } else {

            exchange.sendResponseHeaders(405, -1);

        }

    }

}
