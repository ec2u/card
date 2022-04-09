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

    readonly manager: string;
    readonly version: string;
    readonly instant: string;

    readonly user?: User;
    readonly cards?: Card[];

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
    const [gateway, setGateway]=useStorage<undefined | string>(localStorage, "gateway", undefined);


    useEffect(() => {

        // look in the query string for an auth token forwarded by the SSO gateway

        const token=[location.search.substring(1)].filter(query => query.match(JWTPattern))[0];

        queueMicrotask(() => { // clear the query string

            history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

        });

        if ( !profile || token ) {

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

        }

    }, []);


    function login() {
        if ( gateway ) {
            setProfile(undefined);
            location.replace(`${gateway}?target=${encodeURIComponent(location.href)}`);
        }
    }

    function logout() {
        if ( profile ) {
            setProfile({ ...profile, user: undefined, cards: undefined });
        }
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
