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

import static eu.ec2u.card.RootSetup.setup;
import static eu.ec2u.card.works.ESC.esc;
import static work.BeanFormat.bean;

final class RootHandler extends Handler.Base {

    private final RootSetup setup=service(setup());
    private final ESC esc=service(esc());


    RootHandler() {
        delegate(router()

                .get(this::get)

        );
    }


    private Response get(final Request request) {

        final Root root=new Root()

                .setVersion(setup.getVersion())
                .setInstant(setup.getInstant())

                .setGateway(setup.getGateway());


        return request.user(Profile.class)

                .map(profile -> request.reply(OK)

                        .body(bean(Root.class), root.setProfile(new Profile()

                                .setEsi(profile.getEsi())

                                .setUser(null) // !!!
                                .setCard(esc.cards(profile.getEsi())
                                        .findFirst() // !!! multiple
                                        .orElse(null)
                                )

                        ))

                )

                .orElseGet(() -> request.reply(Unauthorized)

                        .body(bean(Root.class), root)

                );

    }

}
