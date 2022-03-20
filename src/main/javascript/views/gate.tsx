/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { User } from "@ec2u/card/pages/users/user";
import { isPresent } from "@metreeca/core";
import { useProfile } from "@metreeca/nest/keeper";
import * as React from "react";
import { ReactNode } from "react";


export function CardGate({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useProfile<User>();


    function doLogin() { setProfile(); }

    function doLogout() { setProfile(null); }

    return isPresent(profile)
        ? <p>ciao {profile.esi}! <button onClick={doLogout}>logout</button></p>
        : <p>ciao! <button onClick={doLogin}>login</button></p>;

    // return isDefined(profile)
    //     ? <CardVirtual/>
    //     : <CardHome/>;

}