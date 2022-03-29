/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.rest.*;

import eu.ec2u.card.Root.Profile;
import eu.ec2u.card.works.ESC;

import static com.metreeca.rest.Response.OK;
import static com.metreeca.rest.Response.Unauthorized;
import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.handlers.Router.router;

import static eu.ec2u.card.works.ESC.esc;
import static work.BeanFormat.bean;

final class RootHandler extends Handler.Base {

    //private static final String SSO=SAML.Gateway;
    private static final String SSO="https://card.ec2u.eu/eCard/sso";


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

                        .body(bean(Root.class), new Root()
                                .setProfile(new Profile()

                                        .setEsi(profile.getEsi())

                                        .setUser(null) // !!!
                                        .setCard(esc.cards(profile.getEsi())
                                                .findFirst() // !!! multiple
                                                .orElse(null)
                                        )

                                )
                        )

                )

                .orElseGet(() -> request.reply(Unauthorized)

                        .body(bean(Root.class), new Root())

                )

                .map(response -> response

                        .header("Location", SSO)

                );
    }

}
