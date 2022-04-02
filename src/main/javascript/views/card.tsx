/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Profile } from "@ec2u/card/nests/session";
import { About } from "@ec2u/card/pages/about";
import React, { createElement, useState } from "react";
import { Link } from "react-router-dom";
import "./card.css";


/**
 *
 * @see https://router.europeanstudentcard.eu/docs/qrcode-specs / ESC QR specs
 * @see https://github.com/soldair/node-qrcode / npm QR generator
 *
 * @module
 */


export function CardCard() {

    const [profile, setProfile]=useState<Profile>();

    // const data=useRef<HTMLDListElement>(null);
    //
    // const [{ card }, setOptions]=useOptions();
    // const [profile, setProfile]=useProfile<Profile>();
    //
    //
    // useLayoutEffect(() => {
    //
    //     const element=data.current;
    //
    //     if ( element && element.parentElement ) {
    //
    //         const inner=element.getBoundingClientRect();
    //         const outer=element.parentElement.getBoundingClientRect();
    //
    //         const fw=outer.width/inner.width;
    //         const fh=outer.height/inner.height;
    //
    //         element.style.transform=`scale(${Math.min(fw, fh)})`;
    //
    //     }
    //
    // });
    //

    function doFlip() {
        // setOptions({ card: false });
    }

    return createElement("card-xxx", {},

        <>
            <Link to={About.route}>{About.label}</Link>
        </>


        // profile?.card
        //
        // ? <>
        //
        //     {/*<button title={"Home"} onClick={doFlip} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>*/}
        //
        //     {/*{CardPhoto(profile.card)}*/}
        //     {/*<div className={"qr"} title={"QR Code"} style={profile.card ? { backgroundImage: `url('${profile.card.test}')` } : {}}/>*/}
        //     {/*<div className={"hologram"} title={"ESC Hologram"} style={{ backgroundImage: "url('/assets/hologram.png')" }}/>*/}
        //
        //     {/*{profile?.card && CardData(profile.card)}*/}
        //
        // </>
        //
        // : <>
        //
        //     <button title={"Home"} onClick={doFlip} style={{ backgroundImage: "url('/assets/identity.svg')" }}/>
        //
        // </>


    );


    // function CardData({
    //
    //     code,
    //     expiry,
    //
    //     esi,
    //     pic,
    //     level,
    //
    //     name,
    //     photo,
    //     email
    //
    //
    // }: Card) {
    //
    //     const institution="University of Pavia"; // !!!
    //     const countryName="Italy"; // !!!
    //     const countryCode="IT"; // !!!
    //
    //     return <div className={"data"}>
    //
    //         <dl ref={data}>
    //
    //             <dt>Name</dt>
    //             <dd>{name}<br/>{esi
    //                 .replace("urn:schac:personalUniqueCode:int:esi:", "")
    //                 .replace(":", " · ")
    //             }</dd>
    //
    //             <dt>Institution</dt>
    //             <dd>{institution}<br/>{pic}</dd>
    //
    //             <dt>Country</dt>
    //             <dd>{countryName} · {countryCode}</dd>
    //
    //             <dt>Level</dt>
    //             <dd>{CardLevels[level]}</dd>
    //
    //             <dt>Validity</dt>
    //             <dd>{expiry}</dd>
    //
    //         </dl>
    //
    //     </div>;
    // }

    // function CardPhoto({ photo }: Card) {
    //     return <div className={"photo"} title={"Photo ID"} style={photo ? { backgroundImage: `url('${photo}')` } : {}}>{
    //         photo ? <></> : <User/>
    //     }</div>;
    // }

}