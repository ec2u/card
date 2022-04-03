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

import { icon } from "@ec2u/card/index";
import { ChevronLeft, ChevronRight, Heart, LogIn, LogOut, RefreshCw, User } from "lucide-react";
import QRCode from "qrcode";
import React, { createElement, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./card.css";


/**
 *
 * @see https://router.europeanstudentcard.eu/docs/qrcode-specs / ESC QR specs
 * @see https://github.com/soldair/node-qrcode / npm QR generator
 *
 * @module
 */


const JWTPattern=/^(?:[-\w]+\.){2}[-\w]+$/;

const Levels: Record<number, string>={
    6: "Bachelor",
    7: "Master",
    8: "Doctorate"
};


interface Profile {

    readonly version: string;
    readonly instant: string;

    readonly holder?: User;

    readonly cards?: Card[];

}

interface User {

    readonly esi: string;
    readonly uni: string;

}

interface Card {

    readonly code: string;
    readonly test: string;
    readonly expiry: string;

    readonly esi: string;
    readonly pic: number;
    readonly level: number;

    readonly name: string;
    readonly photo?: string;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardCard() {

    const navigate=useNavigate();

    const [gateway, setGateway]=useState<string>(); // the SSO gateway URL provided by the backend
    const [profile, setProfile]=useState<Profile>(); // the current user profile
    const [index, setIndex]=useState(0); // the index of the current profile card

    useEffect(() => {

        // !!! load profile from storage

        // look in query string for an auth token forwarded by the SSO gateway

        const token=[location.search.substring(1)].filter(query => query.match(JWTPattern))[0];

        queueMicrotask(() => { // clear query string

            history.replaceState(history.state, "", location.href.replace(/\?[^#]*/, ""));

        });


        fetch("/", {

            headers: {
                Accept: "application/json",
                Authorization: token ? `Bearer "${token}"` : `Bearer "${"!!!"}"`
            }

        }).then(response => {

            response.json().then((profile: Profile) => { // encode card.test URL into a QR data url

                if ( response.ok ) {

                    if ( profile.cards ) {

                        Promise.all(profile.cards.map(card => {

                            return QRCode.toDataURL(card.test, {

                                errorCorrectionLevel: "M",
                                margin: 0

                            }).then(test => ({ ...card, test }));

                        })).then(cards => setProfile({ ...profile, cards })); // !!! save to storage

                    } else {

                        setProfile(profile); // !!! save to storage

                    }

                } else if ( response.status === 401 ) {

                    setGateway((response.headers.get("WWW-Authenticate") ?? "")
                        .match(/Bearer\s+realm\s*=\s*"(.*)"/)?.[1]
                    );

                    setProfile(undefined);

                } else {

                    // !!! notify

                }

            });

        });

    }, []);


    function doFlip() {
        navigate("/about");
    }

    function doLogIn() {
        if ( gateway ) { location.replace(gateway); }
    }

    function doLogOut() {
        if ( profile ) { setProfile({ ...profile, holder: undefined, cards: undefined });}
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

            : !profile.holder ? <div className={"info"}>

                    <span>Log in to access your ESC cards</span>

                </div>

                : !profile.cards || !profile.cards.length ? <div className={"info"}>

                        <span>Your eduGAIN profile isn't associate with an ESC card</span>
                        <span>Contact your local mobility office to get one</span>

                    </div>

                    : <>

                        {CardData(profile.cards[0])}
                        {CardPhoto(profile.cards[0])}
                        {CardQR(profile.cards[0])}
                        {CardHologram()}

                    </>
        }

    </>);


    function CardLogo() {
        return <a className={"logo"}
            target={"_blank"} href={"https://www.ec2u.eu/"} // !!! from profile
            style={{ backgroundImage: `url('${icon}')` }}
        />;
    }

    function CardMenu() {
        return <div className={"menu"}>

            {!profile ? <button title={"Loading"} className={"spin"}><RefreshCw/></button>

                : !profile.holder ? <button title={"Log in"} onClick={doLogIn}><LogIn/></button>

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

        code,
        expiry,

        esi,
        pic,
        level,

        name,
        photo


    }: Card) {

        const institution="University of Pavia"; // !!!
        const countryName="Italy"; // !!!
        const countryCode="IT"; // !!!

        return <div className={"data"}>

            <dl>

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