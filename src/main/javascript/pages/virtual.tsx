/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Profile } from "@ec2u/card/nests/keeper";
import { useKeeper } from "@metreeca/nest/keeper";
import { LogOut } from "@metreeca/skin/lucide";
import React, { createElement } from "react";
import "./virtual.css";


export function CardVirtual({

    profile

}: {

    profile: Profile

}) {

    const [, setProfile]=useKeeper();


    function doHome() {
        setProfile(false);
    }

    function doLogOut() {
        setProfile(false);
    }


    return createElement("card-virtual", {}, <>

        <button id={"home"} title={"Home"} onClick={doHome} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>
        <button id={"logout"} title={"Close"} onClick={doLogOut}><LogOut/></button>

        <div id={"id"} title={"Photo ID"} style={{ backgroundImage: "url('/assets/mock/photo.png')" }}/>
        <div id={"qr"} title={"QR Code"} style={{ backgroundImage: "url('/assets/mock/qr.png')" }}/>
        <div id={"esc"} title={"ESC Hologram"} style={{ backgroundImage: "url('/assets/hologram.png')" }}/>

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