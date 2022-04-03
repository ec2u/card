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

import { useStorage } from "@ec2u/card/hooks/storage";
import { Profile } from "@ec2u/card/index";
import QRCode from "qrcode";
import { createContext, createElement, ReactNode, useContext, useEffect } from "react";


const JWTPattern=/^(?:[-\w]+\.){2}[-\w]+$/;

const Context=createContext<[undefined | Profile, (action?: null) => void]>([undefined, () => {}]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardProfile({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useStorage<undefined | Profile>(localStorage, "profile", undefined);
    const [gateway, setGateway]=useStorage<undefined | string>(localStorage, "gateway", undefined);


    useEffect(() => {

        // look in the query string for an auth token forwarded by the SSO gateway

        const token=[location.search.substring(1)].filter(query => query.match(JWTPattern))[0];

        queueMicrotask(() => { // clear the query string

            history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

        });


        fetch("/", {

            headers: {
                Accept: "application/json",
                ...(token ? { Authorization: `Bearer "${token}"` } : {})
            }

        }).then(response => {

            response.json().then((profile: Profile) => { // encode card.test URL into a QR data url

                if ( response.ok ) {

                    if ( profile.cards ) {

                        Promise.all(profile.cards.map(card => {

                            return QRCode.toDataURL(card.test, {

                                errorCorrectionLevel: "M",
                                margin: 0

                            }).then(test => ({ ...card, test }));

                        })).then(cards => setProfile({ ...profile, cards }));

                    } else {

                        setProfile(profile);

                    }

                } else if ( response.status === 401 ) {

                    setGateway((response.headers.get("WWW-Authenticate") ?? "")
                        .match(/Bearer\s+realm\s*=\s*"(.*)"/)?.[1]
                    );

                    setProfile(profile);

                } else {

                    // !!! notify

                }

            });

        });

    }, [JSON.stringify(profile)]);


    return createElement(Context.Provider, {

        value: [profile, (action?: null) => {

            if ( action === undefined ) { // login

                if ( gateway ) { location.replace(gateway); }

            } else { // logout

                setProfile(undefined);

            }

        }],

        children

    });

}


export function useProfile<P>(): [undefined | Profile, (profile?: null) => void] {
    return useContext(Context);
}

