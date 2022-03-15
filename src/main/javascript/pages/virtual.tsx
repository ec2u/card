/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import React, { createElement } from "react";
import "./virtual.css";


export function CardVirtual() {

    return createElement("card-virtual", {}, <>

        <button style={{ backgroundImage: "url('/assets/identity.svg')" }}/>

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