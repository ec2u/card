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
import { User } from "lucide-react";
import QRCode from "qrcode";
import { createContext, createElement, ReactNode, useContext, useEffect } from "react";


const JWTPattern=/^(?:[-\w]+\.){2}[-\w]+$/;

const Context=createContext<[undefined | Profile, (action?: null) => void]>([undefined, () => {}]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface Profile {

    readonly info: Info;

    readonly user?: User;
    readonly cards?: Card[];

}

export interface Info {

    readonly version: string;
    readonly instant: string;

}

export interface User {

    readonly esi: string;
    readonly uni: string;

}

export interface Card {

    readonly code: string;
    readonly test: string;
    readonly expiry: string;

    readonly esi: string;
    readonly level: number;
    readonly name: string;
    readonly photo?: string;

    readonly hei: {

        readonly pic: number;
        readonly name: string;

        readonly iso: string;
        readonly country: string;

    };

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardProfile({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useStorage<undefined | Profile>(localStorage, "profile", undefined);


    useEffect(() => {

        // look in the query string for an auth token forwarded by the SSO gateway

        const token=[location.search.substring(1)].filter(query => query.match(JWTPattern))[0];

        queueMicrotask(() => { // clear the query string

            history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

        });

        if ( !profile || token ) {

            fetch("/v1/", {

                headers: {
                    Accept: "application/json",
                    ...(token ? { Authorization: `Bearer ${token}` } : {})
                }

            }).then(response => response.json().then((profile: Profile) => {

                if ( response.ok ) {

                    if ( profile.cards ) { // encode card.test URL into a QR data url

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

                    setProfile(profile);

                } else {

                    // !!! notify

                }


            }));

        }

    }, []);


    function login() {
        location.replace(`/saml/sso?target=${encodeURIComponent(location.href)}&hei=${"unipv.it"}`); // !!! select from profile
    }

    function logout() {
        setProfile(profile?.info ? { info: profile.info } : undefined);
    }


    return createElement(Context.Provider, {

        value: [profile, (action?: null) => {

            if ( action === undefined ) {

                login();

            } else {

                logout();

            }

        }],

        children

    });

}


export function useProfile<P>(): [undefined | Profile, (profile?: null) => void] {
    return useContext(Context);
}
