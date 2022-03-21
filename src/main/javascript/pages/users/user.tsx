/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardPage } from "@ec2u/card/views/page";
import React from "react";


export interface User {

    readonly esi: string;
    readonly admin: boolean;

    readonly forename: string;
    readonly surname: string;

    readonly email: string;

}

export function CardUser() {

    return <CardPage name={"Users › User"}>

    </CardPage>;

}