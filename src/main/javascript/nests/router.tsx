/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardAbout } from "@ec2u/card/pages/about";
import { CardCard } from "@ec2u/card/pages/cards/card";
import { CardCards } from "@ec2u/card/pages/cards/cards";
import { CardContacts } from "@ec2u/card/pages/contacts";
import { CardKey } from "@ec2u/card/pages/keys/token";
import { CardKeys } from "@ec2u/card/pages/keys/tokens";
import { CardPrivacy } from "@ec2u/card/pages/privacy";
import { CardRoot } from "@ec2u/card/pages/root";
import { CardUser } from "@ec2u/card/pages/users/user";
import { CardUsers } from "@ec2u/card/pages/users/users";
import { NodeRouter } from "@metreeca/nest/router";
import * as React from "react";


export function CardRouter() {
    return <NodeRouter routes={{

        "/": CardRoot,

        "/about": CardAbout,
        "/privacy": CardPrivacy,
        "/contacts": CardContacts,

        "/cards/": CardCards,
        "/cards/*": CardCard,

        "/users/": CardUsers,
        "/users/*": CardUser,

        "/keys/": CardKeys,
        "/keys/*": CardKey

    }}/>;
}