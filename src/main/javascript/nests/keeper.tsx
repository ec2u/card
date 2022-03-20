/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Card } from "@ec2u/card/pages/cards/card";
import { User } from "@ec2u/card/pages/users/user";
import { Optional } from "@metreeca/core";
import { useSessionStorage } from "@metreeca/hook/storage";
import { JWTPattern, NodeKeeper } from "@metreeca/nest/keeper";
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

    const [token, setToken]=useSessionStorage("token"); // !!! migrate to NodeFetcher

    useEffect(() => {

        fetch("/", token ? { headers: { Authorization: `Bearer ${token}` } } : {}).then(response => {

            if ( response.ok ) {

                response.json().then(({ profile }) => {

                    setGateway(response.headers.get("Location") ?? undefined);
                    setProfile(profile);

                });

            } else {

                // !!! notify

            }

        });

    }, [token]);

    useEffect(() => {

        [location.search.substring(1)].filter(query => query.match(JWTPattern)).forEach(token => {

            try {

                setToken(token ?? undefined);

            } finally {

                history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

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
