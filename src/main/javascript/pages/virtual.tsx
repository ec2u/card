/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { Profile } from "@ec2u/card/nests/keeper";
import { useOptions } from "@ec2u/card/nests/register";
import { Card, CardLevels } from "@ec2u/card/pages/cards/card";
import { useProfile } from "@metreeca/nest/keeper";
import { User } from "@metreeca/skin/lucide";
import React, { createElement, useLayoutEffect, useRef } from "react";
import "./virtual.css";


export function CardVirtual() {

    const data=useRef<HTMLDListElement>(null);

    const [{ card }, setOptions]=useOptions();
    const [profile, setProfile]=useProfile<Profile>();


    useLayoutEffect(() => {

        const element=data.current;

        if ( element && element.parentElement ) {

            const inner=element.getBoundingClientRect();
            const outer=element.parentElement.getBoundingClientRect();

            const fw=outer.width/inner.width;
            const fh=outer.height/inner.height;

            element.style.transform=`scale(${Math.min(fw, fh)})`;

        }

    });


    function doFlip() {
        setOptions({ card: false });
    }

    return createElement("card-virtual", {}, profile?.card

        ? <>

            <button title={"Home"} onClick={doFlip} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>

            {CardPhoto(profile.card)}
            <div className={"qr"} title={"QR Code"} style={profile.card ? { backgroundImage: `url('${profile.card.test}')` } : {}}/>
            <div className={"hologram"} title={"ESC Hologram"} style={{ backgroundImage: "url('/assets/hologram.png')" }}/>

            {profile?.card && CardData(profile.card)}

        </>

        : <>

            <button title={"Home"} onClick={doFlip} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>


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

        return <div className={"data"}>

            <dl ref={data}>

                <dt>Name</dt>
                <dd>{name}<br/>{esi
                    .replace("urn:schac:personalUniqueCode:int:esi:", "")
                    .replace(":", " · ")
                }</dd>

                <dt>Institution</dt>
                <dd>{institution}<br/>{pic}</dd>

                <dt>Country</dt>
                <dd>{countryName} · {countryCode}</dd>

                <dt>Level</dt>
                <dd>{CardLevels[level]}</dd>

                <dt>Validity</dt>
                <dd>{expiry}</dd>

            </dl>

        </div>;
    }

    function CardPhoto({ photo }: Card) {
        return <div className={"photo"} title={"Photo ID"} style={photo ? { backgroundImage: `url('${photo}')` } : {}}>{
            photo ? <></> : <User/>
        }</div>;
    }

}