/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardOptions } from "@ec2u/card/index";
import { useOptions } from "@ec2u/card/nests/register";
import { CardPage } from "@ec2u/card/views/page";
import { name } from "@metreeca/head";
import React from "react";


export function CardHome() {

    const [{ card }, setX]=useOptions<typeof CardOptions>();


    return <CardPage name={name}>

        {""+card}
        <button onClick={() => setX({ card: !card })}>flip</button>

    </CardPage>;
}