/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.rest.Wrapper;

import eu.ec2u.card.Root.Profile;
import work.BeanCodec;
import work.Notary;

import java.text.ParseException;

import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.Wrapper.postprocessor;
import static com.metreeca.rest.wrappers.Bearer.bearer;

import static work.BeanCodec.codec;
import static work.Notary.notary;

public final class RootKeeper extends Wrapper.Base {

    private final Notary notary=service(notary());
    private final BeanCodec codec=service(codec());

    // !!! error handling


    public RootKeeper() {
        delegate(bearer((token, request) -> notary.verify(token)

                .map((json -> {

                    try {
                        return codec.decode(json, Profile.class);
                    } catch ( final ParseException e ) {
                        return null;
                    }

                }))

                .map(profile -> profile // !!! lookup
                        .setEsi("urn:schac:personalUniqueCode:int:esi:unipv.it:999001")
                )

                .map(request::user)
        )

                .with(postprocessor(response -> response.request().user()
                        .map(profile -> response.header("X-Token", notary.create(codec.encode(profile))))
                        .orElse(response)
                )));
    }


}
