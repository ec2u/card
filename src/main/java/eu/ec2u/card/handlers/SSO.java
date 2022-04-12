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
import com.onelogin.saml2.authn.AuthnRequest;
import com.onelogin.saml2.authn.AuthnRequestParams;
import com.onelogin.saml2.factory.SamlMessageFactory;
import com.onelogin.saml2.settings.Saml2Settings;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.ec2u.card.Work;
import eu.ec2u.card.services.Settings;

import java.io.IOException;
import java.util.*;

import static eu.ec2u.card.Work.query;

import static java.util.Map.entry;

public final class SSO implements HttpHandler {

    private static final SamlMessageFactory samlMessageFactory=new SamlMessageFactory() { };


    @Inject private Settings settings;


    @Override public void handle(final HttpExchange exchange) throws IOException {

        if ( exchange.getRequestMethod().equals("GET") ) {

            final Saml2Settings settings=this.settings.get();


            final Map<String, List<String>> xxx=Optional.ofNullable(exchange.getRequestURI().getQuery())
                    .map(Work::parameters)
                    .orElse(Map.of());


            final String sso=settings.getIdpSingleSignOnServiceUrl().toString();
            final String target=xxx.getOrDefault("target", List.of()).stream().findFirst().orElse("/");

            final AuthnRequest authnRequest=samlMessageFactory.createAuthnRequest(settings,
                    new AuthnRequestParams(false, false, true)
            );


            final Map<String, List<String>> parameters=Map.ofEntries(
                    entry("SAMLRequest", List.of(authnRequest.getEncodedAuthnRequest())),
                    entry("RelayState", List.of(target))
            );

            //if ( settings.getAuthnRequestsSigned() ) {
            //    final String sigAlg=settings.getSignatureAlgorithm();
            //    final String signature=this.buildRequestSignature(samlRequest, relayState, sigAlg);
            //
            //    parameters.put("SigAlg", sigAlg);
            //    parameters.put("Signature", signature);
            //}

            // !!! replay attack?

            //lastRequestId=authnRequest.getId();
            //lastRequestIssueInstant=authnRequest.getIssueInstant();
            //lastRequest=authnRequest.getAuthnRequestXml();

            exchange.getResponseHeaders().set("Location",
                    sso+(sso.contains("?") ? "&" : "?")+query(parameters)
            );

            exchange.sendResponseHeaders(302, -1);


        } else {

            exchange.sendResponseHeaders(405, -1);
        }

    }

}
