import { isDefined } from "@ec2u/card/hooks";
import { useFetcher } from "@ec2u/card/hooks/fetcher";
import { useKeeper } from "@ec2u/card/hooks/keeper";
import * as React from "react";
import { createElement, ReactNode, useEffect } from "react";
import "./gate.css";


interface Profile {

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
            <span>ciao {profile.email}!</span>
            <button onClick={doLogOut}>Log Out</button>
        </>

        : <>
            <span>ciao!</span>
            <button onClick={doLogIn}>Log In</button>
        </>
    );

}