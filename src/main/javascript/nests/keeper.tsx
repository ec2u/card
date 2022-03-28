/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Card } from "@ec2u/card/pages/cards/card";
import { User } from "@ec2u/card/pages/users/user";
import { Optional } from "@metreeca/core";
import { useSessionStorage } from "@metreeca/hook/storage";
import { NodeKeeper } from "@metreeca/nest/keeper";
import QRCode from "qrcode";
import React, { ReactNode, useEffect, useState } from "react";
import { NodeFetcher, useFetcher } from "../@node/nest/fetcher";


const JWTPattern=/^(?:[-\w]+\.){2}[-\w]+$/;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface Profile {

    readonly esi: string;

    readonly user: Optional<User>;
    readonly card: Optional<Card>;

}

export function CardKeeper({

    children

}: {

    children: ReactNode

}) {

    const [, fetcher]=useFetcher();

    const [gateway, setGateway]=useState<string>();
    const [profile, setProfile]=useState<Profile>();

    const [token, setToken]=useSessionStorage<Optional<string>>("token", undefined);

    console.log(token);


    useEffect(() => { // monitor query string for token forwarded by the SSO gateway

        console.log("!!!");

        [location.search.substring(1)].filter(query => query.match(JWTPattern)).forEach(token => {

            try {

                setToken(token ?? undefined);

            } finally {

                history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

            }

        });

        interceptor(fetcher, "/").then(response => {

            if ( response.ok ) {

                response.json().then(({ profile }) => {

                    if ( profile?.card ) { // encode card.test URL into a QR data url

                        QRCode.toDataURL(profile.card.test, {

                            errorCorrectionLevel: "M",
                            margin: 0

                        }).then(test => {

                            setGateway(response.headers.get("Location") ?? undefined);
                            setProfile({ ...profile, card: { ...profile.card, test } });

                        });

                    } else { // !!! on 401?

                        setGateway(response.headers.get("Location") ?? undefined);
                        setProfile(profile);

                    }

                });

            } else {

                // !!! notify

            }

        });

    }, []);


    function login() {
        if ( gateway ) { window.open(`${gateway}?target=${encodeURIComponent(location.href)}`, "_self"); }
    }

    function logout() {
        setProfile(undefined);
        setToken(undefined);
    }

    function interceptor(fetcher: typeof fetch, input: RequestInfo, init: RequestInit={}) {

        const headers=new Headers(init.headers ?? {});

        if ( token ) { headers.set("Authorization", `Bearer ${token}`); }

        return fetcher(input, { ...init, headers }).then(response => {

            setToken(response.headers.get("X-Token") ?? undefined);

            return response;

        });

    }


    return <NodeFetcher interceptor={interceptor}>

        <NodeKeeper state={[profile, (profile?: null | Profile) => {

            if ( profile === undefined ) {

                login();

            } else if ( profile === null ) {

                logout();

            } else {

                setProfile(profile);

            }

        }]}>{

            children

        }</NodeKeeper>

    </NodeFetcher>;

}
