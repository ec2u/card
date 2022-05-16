/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

import { useStorage } from "@ec2u/card/hooks/storage";
import { User } from "lucide-react";
import QRCode from "qrcode";
import { createContext, createElement, ReactNode, useContext, useEffect } from "react";


const API="/v1";
const Login="/Shibboleth.sso/Login?target={}";
const Logout="/Shibboleth.sso/Logout?return={}";

const Context=createContext<[undefined | Profile, (action?: null) => void]>([undefined, () => {}]);


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

export function CardProfile({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useStorage<undefined | Profile>(localStorage, "profile", undefined);


    useEffect(() => {

        if ( !profile ) {

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

                } else {

                    // !!! 409 incomplete eduGAIN profile / unknown tenant / bad ESC credentials
                    // !!! 502 ESC unreachable

                    // !!! 400
                    // !!! 500

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


export function useProfile<P>(): [undefined | Profile, (profile?: null) => void] {
    return useContext(Context);
}
