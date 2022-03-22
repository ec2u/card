/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.rest.Request;
import com.metreeca.rest.Response;
import com.metreeca.rest.handlers.Delegator;

import eu.ec2u.card.Root.Profile;
import eu.ec2u.card.works.ESC;
import eu.ec2u.card.works.SAML;

import static com.metreeca.rest.Response.OK;
import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.formats.JSONFormat.json;
import static com.metreeca.rest.handlers.Router.router;

import static eu.ec2u.card.Root.encode;
import static eu.ec2u.card.works.ESC.esc;

import static java.lang.String.format;

final class RootHandler extends Delegator {

    private static final String Gateway=SAML.Gateway;
    //private static final String Gateway="https://card.ec2u.eu/eCard/sso.php";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final ESC esc=service(esc());


    RootHandler() {
        delegate(router()

                .get(this::get)

        );
    }


    private Response get(final Request request) {
        return request.user(Profile.class)

                .map(profile -> request.reply(OK)

                        .body(json(), encode(new Root()

                                .setProfile(new Profile()

                                        .setEsi(profile.getEsi())

                                        .setUser(null) // !!!
                                        .setCard(esc.cards(profile.getEsi())
                                                .findFirst() // !!! multiple
                                                .orElse(null)
                                        )

                                )

                        ))

                )

                .orElseGet(() -> request.reply(OK)

                        .header("WWW-Authenticate", format("Bearer realm=\"%s\"", request.base()))
                        .header("Location", Gateway)

                        .body(json(), encode(new Root()))

                );
    }

}
