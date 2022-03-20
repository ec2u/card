/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardAbout } from "@ec2u/card/pages/about";
import { CardContacts } from "@ec2u/card/pages/contacts";
import { CardPrivacy } from "@ec2u/card/pages/privacy";
import { CardRoot } from "@ec2u/card/pages/root";
import { NodeRouter } from "@metreeca/nest/router";
import * as React from "react";


export function CardRouter() {
    return <NodeRouter routes={{

        "/": CardRoot,

        "/about": CardAbout,
        "/privacy": CardPrivacy,
        "/contacts": CardContacts

    }}/>;
}