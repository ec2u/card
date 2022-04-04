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

import { useProfile } from "@ec2u/card/hooks/profile";
import { Card, icon } from "@ec2u/card/index";
import { ChevronLeft, ChevronRight, Heart, LogIn, LogOut, RefreshCw, User } from "lucide-react";
import React, { createElement, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./card.css";


/**
 *
 * @see https://router.europeanstudentcard.eu/docs/qrcode-specs / ESC QR specs
 * @see https://github.com/soldair/node-qrcode / npm QR generator
 *
 * @module
 */

const Levels: Record<number, string>={
    6: "Bachelor",
    7: "Master",
    8: "Doctorate"
};


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardCard() {

    const navigate=useNavigate();

    const [profile, setProfile]=useProfile();
    const [index, setIndex]=useState(0); // the index of the current profile card


    function doFlip() {
        navigate("/about");
    }

    function doLogIn() {
        setProfile();
    }

    function doLogOut() {
        setProfile(null);
    }

    function doCycle(delta: number) {
        if ( profile?.cards?.length ) { setIndex((index+delta)%(profile?.cards?.length)); }
    }


    return createElement("card-card", {}, <>

        {CardLogo()}
        {CardMenu()}

        {!profile ? <div className={"info"}>

                <span>Loading your ESC cards</span>

            </div>

            : !profile.user ? <div className={"info"}>

                    <span>Log in to access your ESC cards</span>

                </div>

                : !profile.cards || !profile.cards.length ? <div className={"info"}>

                        <span>Your eduGAIN profile isn't associate with an ESC card</span>
                        <span>Contact your local mobility office to get one</span>

                    </div>

                    : <>

                        {CardData(profile.cards[index])}
                        {CardPhoto(profile.cards[index])}
                        {CardQR(profile.cards[index])}
                        {CardHologram()}

                    </>
        }

    </>);


    function CardLogo() {
        return <a className={"logo"} target={"_blank"} href={profile?.manager || "/"}
            style={{ backgroundImage: `url('${icon}')` }}
        />;
    }

    function CardMenu() {
        return <div className={"menu"}>

            {!profile ? <button title={"Loading"} className={"spin"}><RefreshCw/></button>

                : !profile.user ? <button title={"Log in"} onClick={doLogIn}><LogIn/></button>

                    : <button title={"Log out"} onClick={doLogOut}><LogOut style={{ transform: "scaleX(-1)" }}/></button>
            }

            <button title={"About"} onClick={doFlip}><Heart/></button>

            {profile?.cards && profile.cards.length > 1 && <>
                <button title={"Previous card"} onClick={() => doCycle(-1)}><ChevronLeft/></button>
                <button title={"Next card"} onClick={() => doCycle(+1)}><ChevronRight/></button>
            </>}

        </div>;
    }

    function CardData({

        expiry,

        esi,
        level,
        name,

        hei


    }: Card) {

        return <div className={"data"}>

            <dl>

                <dt>Name</dt>
                <dd>{name}<br/>{esi
                    .replace("urn:schac:personalUniqueCode:int:esi:", "")
                    .replace(":", " · ")
                }</dd>

                <dt>Institution</dt>
                <dd>{hei.name}<br/>{hei.pic}</dd>

                <dt>Country</dt>
                <dd>{hei.country} · {hei.iso}</dd>

                <dt>Level</dt>
                <dd>{Levels[level]}</dd>

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

    function CardQR({ test }: Card) {
        return <div className={"qr"} title={"QR Code"}
            style={{ backgroundImage: `url('${test}')` }}
        />;
    }

    function CardHologram() {
        return <div className={"hologram"} title={"ESC Hologram"}
            style={{ backgroundImage: "url('/assets/hologram.png')" }}
        />;
    }

}