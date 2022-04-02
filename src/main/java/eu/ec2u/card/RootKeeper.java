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
