/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.work;

import com.metreeca.rest.handlers.Delegator;

import com.onelogin.saml2.authn.AuthnRequest;
import com.onelogin.saml2.authn.AuthnRequestParams;
import com.onelogin.saml2.exception.Error;
import com.onelogin.saml2.factory.SamlMessageFactory;
import com.onelogin.saml2.settings.Saml2Settings;
import com.onelogin.saml2.settings.SettingsBuilder;
import com.onelogin.saml2.util.Util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import static com.metreeca.rest.MessageException.status;
import static com.metreeca.rest.Response.Found;
import static com.metreeca.rest.handlers.Router.router;

public final class Session extends Delegator {

    private static final SamlMessageFactory samlMessageFactory=new SamlMessageFactory() { };


    private final Saml2Settings settings=settings();

    private Saml2Settings settings() {

        try {
            final Saml2Settings settings=new SettingsBuilder().fromFile("saml.properties").build();

            //final List<String> settingsErrors = settings.checkSettings();
            //if (!settingsErrors.isEmpty()) {
            //    String errorMsg = "Invalid settings: ";
            //    errorMsg += StringUtils.join(settingsErrors, ", ");
            //    LOGGER.error(errorMsg);
            //    throw new SettingsException(errorMsg, SettingsException.SETTINGS_INVALID);
            //}
            //LOGGER.debug("Settings validated");

            return settings;

        } catch ( final Error e ) {

            throw new RuntimeException(e); // !!!

        } catch ( final IOException e ) {

            throw new UncheckedIOException(e);


        }
    }


    public Session() {
        delegate(router()

                .path("/login", request -> { // !!! method

                    try {

                        final AuthnRequestParams authnRequestParams=new AuthnRequestParams(false, false, true);


                        final AuthnRequest authnRequest=samlMessageFactory.createAuthnRequest(settings,
                                authnRequestParams);


                        final Map<String, String> parameters=new HashMap<>();


                        final String samlRequest=authnRequest.getEncodedAuthnRequest();

                        parameters.put("SAMLRequest", samlRequest);
                        parameters.put("RelayState", request.item());


                        //if ( settings.getAuthnRequestsSigned() ) {
                        //    final String sigAlg=settings.getSignatureAlgorithm();
                        //    final String signature=this.buildRequestSignature(samlRequest, relayState, sigAlg);
                        //
                        //    parameters.put("SigAlg", sigAlg);
                        //    parameters.put("Signature", signature);
                        //}

                        final String ssoUrl=settings.getIdpSingleSignOnServiceUrl().toString();

                        //lastRequestId=authnRequest.getId();
                        //lastRequestIssueInstant=authnRequest.getIssueInstant();
                        //lastRequest=authnRequest.getAuthnRequestXml();

                        //if ( !stay ) {
                        //    LOGGER.debug("AuthNRequest sent to "+ssoUrl+" --> "+samlRequest);
                        //}

                        String target=ssoUrl;

                        if ( !parameters.isEmpty() ) {
                            boolean first=!ssoUrl.contains("?");
                            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                                if ( first ) {
                                    target+="?";
                                    first=false;
                                } else {
                                    target+="&";
                                }
                                target+=parameter.getKey();
                                if ( !parameter.getValue().isEmpty() ) {
                                    target+="="+Util.urlEncoder(parameter.getValue());
                                }
                            }
                        }

                        return request.reply(status(Found, target));

                    } catch ( final IOException e ) {
                        throw new UncheckedIOException(e);
                    }

                })

        );
    }

}
