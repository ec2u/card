/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.rest.Handler;
import com.metreeca.rest.Wrapper;

import eu.ec2u.card.Root.Profile;
import work.Notary;

import javax.json.JsonValue;

import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.wrappers.Bearer.bearer;

import static work.Notary.notary;

public final class RootKeeper implements Wrapper {


    private final Notary notary=service(notary());

    // !!! error handling

    private final Wrapper delegate=bearer((token, request) -> notary.verify(token)
            .map(Notary::decode)
            .map(JsonValue::asJsonObject)
            .map(Profile::decode)
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
