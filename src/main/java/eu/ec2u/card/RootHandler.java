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
