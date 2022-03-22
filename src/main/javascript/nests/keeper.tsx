/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Card } from "@ec2u/card/pages/cards/card";
import { User } from "@ec2u/card/pages/users/user";
import { Optional } from "@metreeca/core";
import { useSessionStorage } from "@metreeca/hook/storage";
import { JWTPattern, NodeKeeper } from "@metreeca/nest/keeper";
import QRCode from "qrcode";
import React, { ReactNode, useEffect, useState } from "react";


export interface Profile {

    readonly user: Optional<User>;
    readonly card: Optional<Card>;

}

export function CardKeeper({

    children

}: {

    children: ReactNode

}) {

    const [gateway, setGateway]=useState<string>();
    const [profile, setProfile]=useState<User>();

    const [token, setToken]=useSessionStorage<Optional<string>>("token", undefined); // !!! migrate to NodeFetcher

    useEffect(() => {

        [location.search.substring(1)].filter(query => query.match(JWTPattern)).forEach(token => {

            try {

                setToken(token ?? undefined);

            } finally {

                history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

            }

        });

    }, [token]);

    useEffect(() => {

        // !!! move token management to interceptor

        fetch("/", token ? {

            headers: {
                Authorization: `Bearer ${token}`,
                Accept: "applications/json",
                "Cache-Control": "no-cache, max-age=0, must-revalidate"
            }

        } : {}).then(response => {

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

                    } else { // !!! on 401

                        setGateway(response.headers.get("Location") ?? undefined);
                        setProfile(profile);

                    }

                });

            } else {

                // !!! notify

            }

        });

    }, [token]);


    function login() {
        if ( gateway ) { window.open(`${gateway}?target=${encodeURIComponent(location.href)}`, "_self"); }
    }

    function logout() {
        setToken(undefined);
    }


    return <NodeKeeper state={[profile, (profile?: null | User) => {

        if ( profile === undefined ) {

            login();

        } else if ( profile === null ) {

            logout();

        }

    }]}>{

        children

    }</NodeKeeper>;

}
