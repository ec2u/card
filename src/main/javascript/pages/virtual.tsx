/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { isDefined } from "@metreeca/core";
import { useKeeper } from "@metreeca/nest/keeper";
import { useRouter } from "@metreeca/nest/router";
import { LogOut } from "@metreeca/skin/lucide";
import React, { createElement, useEffect } from "react";
import "./virtual.css";


export function CardVirtual() {

    const [profile, setProfile]=useKeeper();
    const [, setRoute]=useRouter();

    console.log(profile);

    useEffect(() => {

        if ( !isDefined(profile) ) { setProfile(true); }

    }, [profile]);


    function doLogOut() {
        // setProfile(false);
        setRoute("/");
    }


    return createElement("card-virtual", {}, <>

        <a title={"Home"} href={"/"} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>
        <button title={"Close"} onClick={doLogOut}><LogOut/></button>

        <div title={"Photo ID"} style={{ backgroundImage: "url('/assets/mock/photo.png')" }}/>
        <div title={"QR Code"} style={{ backgroundImage: "url('/assets/mock/qr.png')" }}/>
        <div title={"ESC Hologram"} style={{ backgroundImage: "url('/assets/hologram.png')" }}/>

        <dl>

            <dt>Name</dt>
            <dd>Hermione Granger<br/>123-4567-890</dd>

            <dt>Institution</dt>
            <dd>University of Pavia<br/>999893752</dd>

            <dt>Country</dt>
            <dd>Italy<br/>IT</dd>

        </dl>

        <dl>

            <dt>Level</dt>
            <dd>Master</dd>

            <dt>Validity</dt>
            <dd>2023-03-15</dd>

        </dl>

    </>);

}