/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.works;

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
import eu.ec2u.card.RootServer;
import eu.ec2u.card.users.Users.User;
import work.Notary;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.cert.CertificateEncodingException;
import java.util.*;

import static com.metreeca.core.Lambdas.checked;
import static com.metreeca.rest.Response.*;
import static com.metreeca.rest.formats.TextFormat.text;
import static com.metreeca.rest.handlers.Router.router;

import static work.Query.query;

import static java.lang.String.format;
import static java.util.Map.entry;

public final class SAML extends Delegator {

    public static final String Pattern="/saml/*";
    public static final String Session="/saml/session";


    private static final Saml2Settings settings=settings();
    private static final SamlMessageFactory samlMessageFactory=new SamlMessageFactory() { };

    private static final Notary notary=new Notary(RootServer.JWTKey);


    private static Saml2Settings settings() {

        try {

            final Saml2Settings settings=new SettingsBuilder().fromFile("eu/ec2u/card/works/SAML.properties").build();

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public SAML() {
        delegate(router()

                .path("/", router().get(this::metadata))
                .path("/acs", router().post(this::acs))
                .path("/session", router().get(this::session))

        );
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

            return request.reply(OK)
                    .header("Content-Type", XMLFormat.MIME)
                    .body(text(), settings.getSPMetadata());

        } catch ( final CertificateEncodingException e ) {
            throw new RuntimeException(e); // !!!
        }
    }

    private Response acs(final Request request) {

        final HttpRequest http=new HttpRequest(request.item(), request.parameters(), request.query());

        return Optional.ofNullable(http.getParameter("SAMLResponse"))

                .map(checked(samlResponse -> { return samlMessageFactory.createSamlResponse(settings, http); }))

                .map(checked(samlResponse -> {

                    // !!! replay attack?

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

                        final String token=notary.create(Notary.encode(User.encode(new User()
                                .setEsi(esi)
                        )));

                        return request.reply(Found, format("%s?%s",
                                request.parameter("RelayState").orElse("/"),
                                token
                        ));

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

                        return null;
                    }


                }))

                .orElseGet(() -> {

                    //errors.add("invalid_binding");
                    //final String errorMsg = "SAML Response not found, Only supported HTTP_POST Binding";
                    //LOGGER.error("processResponse error." + errorMsg);
                    //throw new Error(errorMsg, Error.SAML_RESPONSE_NOT_FOUND);

                    return request.reply(BadRequest);

                });

    }

    private Response session(final Request request) {
        try {

            final String sso=settings.getIdpSingleSignOnServiceUrl().toString();
            final String target=request.parameter("target").orElse("/");

            final AuthnRequest authnRequest=samlMessageFactory.createAuthnRequest(settings,
                    new AuthnRequestParams(false, false, true)
            );


            final Map<String, String> parameters=Map.ofEntries(
                    entry("SAMLRequest", authnRequest.getEncodedAuthnRequest()),
                    entry("RelayState", target)
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

            return request.reply(Found, query(sso, parameters));

        } catch ( final IOException e ) {
            throw new UncheckedIOException(e);
        }
    }

}
