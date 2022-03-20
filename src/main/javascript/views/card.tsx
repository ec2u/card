/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Profile } from "@ec2u/card/nests/keeper";
import { Card, CardLevels } from "@ec2u/card/pages/cards/card";
import { useProfile } from "@metreeca/nest/keeper";
import { LogOut, User } from "@metreeca/skin/lucide";
import React, { createElement } from "react";
import "./card.css";


export function CardVirtual() {

    const [profile, setProfile]=useProfile<Profile>();


    console.log(profile?.card?.test);


    function doLogOut() {
        setProfile(null);
    }

    return createElement("card-virtual", {}, profile?.card

        ? <>

            <a title={"Home"} href={"/"} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>
            <button title={"Close"} onClick={doLogOut}><LogOut/></button>

            {CardPhoto(profile.card)}
            <div className={"qr"} title={"QR Code"} style={profile.card ? { backgroundImage: `url('${profile.card.test}')` } : {}}/>
            <div className={"hologram"} title={"ESC Hologram"} style={{ backgroundImage: "url('/assets/hologram.png')" }}/>

            {profile?.card && CardData(profile.card)}

        </>

        : <>

        </>
    );


    function CardData({

        code,
        expiry,

        esi,
        pic,
        level,

        name,
        photo,
        email


    }: Card) {

        const institution="University of Pavia"; // !!!
        const countryName="Italy"; // !!!
        const countryCode="IT"; // !!!

        return <dl>

            <dt>Name</dt>
            <dd>{name}<br/>{esi.replace("urn:schac:personalUniqueCode:int:esi:", "")}</dd>

            <dt>Institution</dt>
            <dd>{institution}<br/>{pic}</dd>

            <dt>Country</dt>
            <dd>{countryName}<br/>{countryCode}</dd>

            <dt>Level</dt>
            <dd>{CardLevels[level]}</dd>

            <dt>Validity</dt>
            <dd>{expiry}</dd>

        </dl>;
    }

    function CardPhoto({ photo }: Card) {
        return <div className={"photo"} title={"Photo ID"} style={photo ? { backgroundImage: `url('${photo}')` } : {}}>{
            photo ? <></> : <User/>
        }</div>;
    }

}