/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Card } from "@ec2u/card/pages/cards/card";
import { User } from "@ec2u/card/pages/users/user";
import { isFunction, Optional } from "@metreeca/core";
import { useSessionStorage } from "@metreeca/hook/storage";
import { NodeKeeper } from "@metreeca/nest/keeper";
import QRCode from "qrcode";
import React, { ReactNode, useState } from "react";
import { NodeFetcher } from "../@node/nest/fetcher";


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

    const [gateway, setGateway]=useState<string>(); // the SSO gateway URL provided by the backend
    const [profile, setProfile]=useState<Profile>(); // the current user profile

    const [token, setToken]=useSessionStorage<Optional<string>>("token", () => { // the current access token

        try { // look in query string for an auth token forwarded by the SSO gateway

            return [location.search.substring(1)].filter(query => query.match(JWTPattern))[0];

        } finally { // clear query string

            queueMicrotask(() => {

                history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

            });

        }

    });


    function login() {
        if ( gateway ) {

            setProfile(undefined);
            setToken(undefined);

            window.open(`${gateway}?target=${encodeURIComponent(location.href)}`, "_self");

        }
    }

    function logout() {
        if ( gateway ) {

            setProfile(undefined);
            setToken(undefined);

        }
    }


    return <NodeFetcher interceptor={(fetcher, input, init={}) => {

        const headers=new Headers(init.headers ?? {});

        if ( token ) { headers.set("Authorization", `Bearer ${token}`); }

        return fetcher(input, { ...init, headers }).then(response => {

            setToken(response.headers.get("X-Token") ?? undefined);

            return response;

        });

    }}>

        <NodeKeeper state={[profile, (profile?: null | Profile | typeof fetch) => {

            if ( isFunction(profile) ) {

                profile("/").then(response => {

                    if ( response.ok ) {

                        response.json().then(({ profile }) => { // encode card.test URL into a QR data url

                            setGateway(response.headers.get("Location") ?? undefined);

                            if ( profile?.card ) {

                                QRCode.toDataURL(profile.card.test, {

                                    errorCorrectionLevel: "M",
                                    margin: 0

                                }).then(test => {

                                    setProfile({ ...profile, card: { ...profile.card, test } });

                                });

                            } else { // !!! on 401?

                                setProfile(profile);

                            }

                        });

                    } else {

                        // !!! notify

                    }

                });

            } else if ( profile === undefined ) {

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
