/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import React, { createElement, useState } from "react";


export const LinePattern="\\S+( \\S+)*";

export interface User {

    readonly esi: string;
    readonly admin: boolean;

    readonly forename: string;
    readonly surname: string;

    readonly email: string;

}

export function CardUser() {

    const [user, setUser]=useState();

    return createElement("card-user", {},

        <form>

            <input type={"text"} value={user?.[0]} pattern={LinePattern}/>

        </form>
    );
}