/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardOptions } from "@ec2u/card/index";
import { useOptions } from "@ec2u/card/nests/register";
import { CardHome } from "@ec2u/card/pages/home";
import { CardVirtual } from "@ec2u/card/pages/virtual";
import React from "react";


export function CardRoot() {

    const [{ card }, setOptions]=useOptions<typeof CardOptions>();


    return card
        ? <CardVirtual/>
        : <CardHome/>;

}