/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.work;

import com.metreeca.rest.Request;
import com.metreeca.rest.Response;
import com.metreeca.rest.handlers.Delegator;
import com.metreeca.xml.formats.XMLFormat;

import com.onelogin.saml2.authn.AuthnRequest;
import com.onelogin.saml2.authn.AuthnRequestParams;
import com.onelogin.saml2.exception.Error;
import com.onelogin.saml2.factory.SamlMessageFactory;
import com.onelogin.saml2.http.HttpRequest;
import com.onelogin.saml2.settings.Saml2Settings;
import com.onelogin.saml2.settings.SettingsBuilder;
import com.onelogin.saml2.util.Util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.cert.CertificateEncodingException;
import java.util.*;

import static com.metreeca.core.Lambdas.checked;
import static com.metreeca.rest.MessageException.status;
import static com.metreeca.rest.Response.*;
import static com.metreeca.rest.formats.TextFormat.text;
import static com.metreeca.rest.handlers.Router.router;

public final class SAML extends Delegator {

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


    public SAML() {
        delegate(router()

                .path("/", router().get(this::metadata))
                .path("/acs", router().post(this::acs))
                .path("/login", router().get(this::login))

        );
    }


    private Response acs(final Request request) {


        final HttpRequest http=new HttpRequest(request.item(), request.parameters(), request.query());

        return Optional.ofNullable(http.getParameter("SAMLResponse"))


                .map(checked(samlResponse -> { return samlMessageFactory.createSamlResponse(settings, http); }))

                .map(checked(samlResponse -> {

                    //lastResponse = samlResponse.getSAMLResponseXml();

                    if ( samlResponse.isValid(null) ) {

                        //nameid = samlResponse.getNameId();
                        //nameidFormat = samlResponse.getNameIdFormat();
                        //nameidNameQualifier = samlResponse.getNameIdNameQualifier();
                        //nameidSPNameQualifier = samlResponse.getNameIdSPNameQualifier();
                        //authenticated = true;
                        //attributes = samlResponse.getAttributes();
                        //sessionIndex = samlResponse.getSessionIndex();
                        //sessionExpiration = samlResponse.getSessionNotOnOrAfter();
                        //lastMessageId = samlResponse.getId();
                        //lastMessageIssueInstant = samlResponse.getResponseIssueInstant();
                        //lastAssertionId = samlResponse.getAssertionId();
                        //lastAssertionNotOnOrAfter = samlResponse.getAssertionNotOnOrAfter();

                        //LOGGER.debug("processResponse success --> " + samlResponseParameter);

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
                    }

                    return request.reply(status(Found, "/in"));


                    //final String relayState = http.getParameter("RelayState");
                    //
                    //if (relayState != null && relayState != ServletUtils.getSelfRoutedURLNoQuery(request)) {
                    //    response.sendRedirect(request.getParameter("RelayState"));
                    //} else {
                    //    if (attributes.isEmpty()) {
                    //        out.println("You don't have any attributes");
                    //    }
                    //    else {
                    //        final Collection<String> keys = attributes.keySet();
                    //        for(final String name :keys){
                    //            out.println(name);
                    //            final List<String> values = attributes.get(name);
                    //            for(final String value :values) {
                    //                out.println(" - " + value);
                    //            }
                    //        }
                    //    }
                    //}


                }))

                .orElseGet(() -> {

                    //errors.add("invalid_binding");
                    //final String errorMsg = "SAML Response not found, Only supported HTTP_POST Binding";
                    //LOGGER.error("processResponse error." + errorMsg);
                    //throw new Error(errorMsg, Error.SAML_RESPONSE_NOT_FOUND);


                    return request.reply(status(BadRequest));
                });


    }


    private Response metadata(final Request request) {

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

        try {

            return request.reply(status(OK))
                    .header("Content-Type", XMLFormat.MIME)
                    .body(text(), settings.getSPMetadata());

        } catch ( final CertificateEncodingException e ) {
            throw new RuntimeException(e); // !!!
        }
    }


    private Response login(final Request request) {
        try {

            final AuthnRequestParams authnRequestParams=new AuthnRequestParams(false, false, true);
            final AuthnRequest authnRequest=samlMessageFactory.createAuthnRequest(settings, authnRequestParams);

            final Map<String, String> parameters=new HashMap<>();

            parameters.put("SAMLRequest", authnRequest.getEncodedAuthnRequest());

            // !!! integrated into request.item()
            // !!! https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Forwarded

            parameters.put("RelayState", String.format("%s://%s%s",
                    request.header("X-Forwarded-Proto").orElseThrow(),
                    request.header("X-Forwarded-Host").orElseThrow(),
                    request.path()
            ));


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

            // !!! factor

            String target=ssoUrl;

            if ( !parameters.isEmpty() ) {
                boolean first=!ssoUrl.contains("?");
                for (final Map.Entry<String, String> parameter : parameters.entrySet()) {
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
    }

}
