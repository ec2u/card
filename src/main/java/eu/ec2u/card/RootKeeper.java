/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.rest.Handler;
import com.metreeca.rest.Wrapper;

import eu.ec2u.card.Root.Profile;
import work.BeanCodec;
import work.Notary;

import java.text.ParseException;

import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.wrappers.Bearer.bearer;

import static work.BeanCodec.codec;
import static work.Notary.notary;

public final class RootKeeper implements Wrapper {


    private final Notary notary=service(notary());
    private final BeanCodec codec=service(codec());

    // !!! error handling

    private final Wrapper delegate=bearer((token, request) -> notary.verify(token)
            .map((json -> {

                try {
                    return codec.decode(json, Profile.class);
                } catch ( final ParseException e ) {
                    return null;
                }

            }))

            .map(request::user)
    );


    @Override public Handler wrap(final Handler handler) {

        if ( handler == null ) {
            throw new NullPointerException("null handler");
        }

        return delegate.wrap(handler);
    }

    @Override public Wrapper with(final Wrapper wrapper) {

        if ( wrapper == null ) {
            throw new NullPointerException("null wrapper");
        }

        return delegate.with(wrapper);
    }

}
