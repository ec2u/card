/*
 * Copyright © 2020-2025 EC2U Alliance
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


const API="/v1";
const Login="/Shibboleth.sso/Login?target={}";
const Logout="/Shibboleth.sso/Logout?return={}";

const Context=createContext<[undefined | Profile | Error, (action?: null) => void]>([undefined, () => {}]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface Profile {

    readonly user?: User;
    readonly cards?: Card[];

}

export interface User {

    readonly esi: string;
    readonly name: string;
    readonly email: string;
    readonly schac: string;

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
        readonly schac: string;

        readonly name: string;
        readonly home: string;
        readonly logo: string;

        readonly iso: string;
        readonly country: string;

    };

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


export interface Error {

    readonly status: number;
    readonly reason: string;

}


export function isError(value: unknown): value is Error {
    return typeof value === "object" && value !== null && "status" in value && "reason" in value;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardProfile({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useStorage<undefined | Profile | Error>(localStorage, "profile", undefined);


    useEffect(() => {

        if ( !profile || isError(profile) ) {

            fetch(API, {

                headers: { Accept: "application/json" }

            }).then(response => {

                if ( response.ok ) {

                    response.json().then((profile: Profile) => {

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

                    });

                } else if ( response.status === 401 ) {

                    setProfile({});


                } else if ( response.status === 409 ) {

                    response.json().then((reason: string) => {

                        setProfile({ status: response.status, reason });

                    });

                } else if ( response.status === 502 ) {

                    setProfile({ status: response.status, reason: "esc-unreachable" });

                } else {

                    setProfile({ status: response.status, reason: "internal" });

                }

            });

        }

    }, []);


    function login() {
        try {

            setProfile(undefined);

        } finally {

            location.replace(Login.replace("{}", encodeURIComponent(location.pathname)));

        }
    }

    function logout() {
        try {

            setProfile(undefined);

        } finally {

            location.replace(Logout.replace("{}", encodeURIComponent(location.origin)));

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


export function useProfile(): [undefined | Profile | Error, (profile?: null) => void] {
    return useContext(Context);
}
