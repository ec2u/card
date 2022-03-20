/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { User } from "@ec2u/card/pages/users/user";
import { JWTPattern, NodeKeeper } from "@metreeca/nest/keeper";
import React, { ReactNode, useEffect, useState } from "react";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardKeeper({

    children

}: {

    children: ReactNode

}) {

    const [gate, setGate]=useState<string>();
    const [user, setUser]=useState<User>();

    const [token, setToken]=useState<string>(); // !!! migrate to fetcher

    useEffect(() => {

        fetch("/", token ? { headers: { Authorization: `Bearer ${token}` } } : {}).then(response => {

            if ( response.ok ) {

                response.json().then(({ user }) => {

                    setGate(response.headers.get("Location") ?? undefined);
                    setUser(user ?? undefined);

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
        if ( gate ) { window.open(`${gate}?target=${encodeURIComponent(location.href)}`, "_self"); }
    }

    function logout() {
        setToken(undefined);
    }


    return <NodeKeeper state={[user, (profile?: null | User) => {

        if ( profile === undefined ) {

            login();

        } else if ( profile === null ) {

            logout();

        } else {

            setUser(profile);

        }

    }]}>{

        children

    }</NodeKeeper>;

}
