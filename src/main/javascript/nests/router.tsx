/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardAbout } from "@ec2u/card/pages/about";
import { CardContacts } from "@ec2u/card/pages/contacts";
import { CardHome } from "@ec2u/card/pages/home";
import { CardPrivacy } from "@ec2u/card/pages/privacy";
import { CardVirtual } from "@ec2u/card/views/card";
import { NodeRouter } from "@metreeca/nest/router";
import * as React from "react";


export function CardRouter() {
    return <NodeRouter routes={{

        "/": CardHome,
        "/me": CardVirtual,

        "/about": CardAbout,
        "/privacy": CardPrivacy,
        "/contacts": CardContacts

    }}/>;
}