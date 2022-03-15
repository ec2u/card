/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { NodeKeeper } from "@metreeca/nest/keeper";
import React, { ReactNode } from "react";


export interface Profile {

    readonly code: string;
    readonly email: string;

}


export function CardKeeper({

    children

}: {

    children: ReactNode

}) {

    function test() {
        return fetch("/").then(response => {

            if ( response.ok ) {

                return response.json().then(({ profile }) => profile);

            } else {

                // !!! notify

                return undefined;

            }

        });
    }


    function login() {
        return Promise.resolve(undefined).then(() => {

            location.assign("/login");

        });
    }

    function logout() {
        return fetch("/logout", { method: "POST" }).then(response => {

            if ( !response.ok ) {

                // !!! notify

            }

            return undefined;

        });
    }


    return <NodeKeeper actions={{ test, login, logout }}>{

        children

    }</NodeKeeper>;

}
