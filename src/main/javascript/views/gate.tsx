/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Profile } from "@ec2u/card/nests/keeper";
import { CardHome } from "@ec2u/card/pages/home";
import { CardVirtual } from "@ec2u/card/pages/virtual";
import { isDefined } from "@metreeca/core";
import { useKeeper } from "@metreeca/nest/keeper";
import * as React from "react";
import { ReactNode } from "react";


export function CardGate({

    children

}: {

    children: ReactNode

}) {

    const [profile]=useKeeper<Profile>();

    return isDefined(profile)
        ? <CardVirtual profile={profile}/>
        : <CardHome/>;

}