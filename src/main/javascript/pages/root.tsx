/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardHome } from "@ec2u/card/pages/home";
import { User } from "@ec2u/card/pages/users/user";
import { CardVirtual } from "@ec2u/card/views/card";
import { isPresent } from "@metreeca/core";
import { useProfile } from "@metreeca/nest/keeper";
import React from "react";


export function CardRoot() {

    const [profile, setProfile]=useProfile<User>();


    function doLogin() { setProfile(); }

    function doLogout() { setProfile(null); }

    return isPresent(profile)
        ? <CardVirtual/>
        : <CardHome/>;

}