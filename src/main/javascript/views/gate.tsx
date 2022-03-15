/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { isDefined } from "@metreeca/core";
import { useFetcher } from "@metreeca/nest/fetcher";
import { useKeeper } from "@metreeca/nest/keeper";
import * as React from "react";
import { createElement, ReactNode, useEffect } from "react";
import "./gate.css";


interface Profile {

    readonly code: string;
    readonly email: string;

}


export function CardGate({

    children

}: {

    children: ReactNode

}) {

    const [fetching, fetch]=useFetcher();
    const [profile, setProfile]=useKeeper<Profile>();

    useEffect(() => {

        if ( !isDefined(profile) ) {
            fetch("/").then(response => {

                if ( response.ok ) {

                    response.json().then(({ profile }) => setProfile(profile));

                } else {

                    // !!! notify

                }

            });
        }

    }, [profile]);


    function doLogIn() {
        location.assign("/login");
    }

    function doLogOut() {
        fetch("/logout", { method: "POST" }).finally(() => {

            setProfile(undefined);

        });
    }


    return createElement("card-gate", {}, isDefined(profile)

        ? <>
            <span>ciao {profile.code}!</span>
            <button onClick={doLogOut}>Log Out</button>
        </>

        : <>
            <span>ciao!</span>
            <button onClick={doLogIn}>Log In</button>
        </>
    );

}