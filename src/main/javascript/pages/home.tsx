/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardOptions } from "@ec2u/card/index";
import { useOptions } from "@ec2u/card/nests/register";
import { CardPage } from "@ec2u/card/views/page";
import { useProfile } from "@metreeca/nest/keeper";
import { Contact } from "@metreeca/skin/lucide";
import React from "react";


export function CardHome() {

    const [profile, setProfile]=useProfile();
    const [{ card }, setOptions]=useOptions<typeof CardOptions>();


    return <CardPage>

        {profile ? <button onClick={() => setOptions({ card: !card })} style={{

            display: "block",
            margin: "10% auto",
            transform: "scale(5)"

        }}><Contact/></button> : null}

    </CardPage>;
}