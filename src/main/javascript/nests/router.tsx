/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { about, CardAbout } from "@ec2u/card/pages/about";
import { CardCard } from "@ec2u/card/pages/cards/card";
import { CardCards } from "@ec2u/card/pages/cards/cards";
import { CardContacts, contacts } from "@ec2u/card/pages/contacts";
import { CardKey } from "@ec2u/card/pages/keys/token";
import { CardKeys } from "@ec2u/card/pages/keys/tokens";
import { CardPrivacy, privacy } from "@ec2u/card/pages/privacy";
import { CardRoot, root } from "@ec2u/card/pages/root";
import { CardUser } from "@ec2u/card/pages/users/user";
import { CardUsers } from "@ec2u/card/pages/users/users";
import { NodeRouter } from "@metreeca/nest/router";
import * as React from "react";


export function CardRouter() {
    return <NodeRouter routes={{

        [root.id]: CardRoot,

        [about.id]: CardAbout,
        [privacy.id]: CardPrivacy,
        [contacts.id]: CardContacts,

        "/cards/": CardCards,
        "/cards/*": CardCard,

        "/users/": CardUsers,
        "/users/*": CardUser,

        "/keys/": CardKeys,
        "/keys/*": CardKey

    }}/>;
}