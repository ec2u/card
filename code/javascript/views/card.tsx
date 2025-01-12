/*
 * Copyright © 2020-2025 EC2U Alliance
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

import { Card, isError, useProfile } from "@ec2u/card/hooks/profile";
import { page } from "@ec2u/card/index";
import { ChevronLeft, ChevronRight, Info, LogIn, LogOut, RefreshCw, User } from "lucide-react";
import React, { createElement, useEffect, useRef, useState } from "react";
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

    const card=useRef<HTMLElement>(null);
    const data=useRef<HTMLDListElement>(null);


    function size() {

        if ( card.current ) {

            const vw=window.innerWidth/100;
            const vh=window.innerHeight/100;

            card.current.style.setProperty("--vw", `${vw}px`);
            card.current.style.setProperty("--vh", `${vh}px`);
            card.current.style.setProperty("--vmin", `${Math.min(vw, vh)}px`);
            card.current.style.setProperty("--vmax", `${Math.max(vw, vh)}px`);
        }

    }

    function fit() {

        if ( data.current ) {

            data.current.style.transform=`scale(1)`;

            const inner=data.current.getBoundingClientRect();
            const outer=data.current.parentElement?.getBoundingClientRect() ?? inner;

            const fw=inner.width === 0 ? 1 : outer.width/inner.width;
            const fh=inner.height === 0 ? 1 : outer.height/inner.height;

            data.current.style.transform=`scale(${Math.min(fw, fh)})`;

        }

    }

    useEffect(() => {

        size();
        fit();

    });

    useEffect(() => {

        window.addEventListener("resize", size);
        window.addEventListener("resize", fit);

        return () => {
            window.removeEventListener("resize", size);
            window.removeEventListener("resize", fit);
        };

    }, []);


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
        if ( !isError(profile) && profile?.cards?.length ) { setIndex((index+delta)%(profile?.cards?.length)); }
    }


    return createElement("card-card", {

        ref: card

    }, <>

        <nav>

            {CardLogo()}
            {!isError(profile) && profile?.cards?.length && CardHome(profile.cards[index])}
            {CardMenu()}

        </nav>

        {!profile ? <div>

                <span>Loading your ESC&nbsp;cards</span>

            </div>

            : isError(profile) ? <div>

                    <span>:-(</span>

                    {

                        profile.status === 409 && profile.reason.startsWith("edugain-") ? <>

                                <span>Your eduGAIN profile doesn't contain some required field</span>
                                <span><a href={"/contacts"}>Contact</a> your local IT staff to complete&nbsp;it</span>

                            </>

                            : profile.status === 409 && profile.reason === "tenant-undefined" ? <>

                                    <span>Your organization is not registered</span>
                                    <span><a href={"/contacts"}>Contact</a> your local IT staff to activate&nbsp;it</span>

                                </>

                                : profile.status === 502 ? <>

                                        <span>Unable to contact ESC to get card details</span>
                                        <span>Please try again later and if the issue
                                            persists <a href={"/contacts"}>report</a> it to your local IT staff
                                        </span>

                                    </>

                                    : <>
                                        <span>Internal error</span>
                                        <span>Please <a href={"/contacts"}>report</a> it to your local IT staff</span>
                                        <span>Thanks!</span>
                                    </>

                    }

                </div>

                : !profile.user ? <div>

                        <span>Log in to access your ESC&nbsp;cards</span>

                    </div>

                    : !profile.cards?.length ? <div>

                            <span>Your eduGAIN profile isn't associated with an ESC&nbsp;card</span>
                            <span>Contact your local mobility office to get&nbsp;one</span>

                        </div>

                        : <>

                            <main>
                                {CardData(profile.cards[index])}
                            </main>

                            <aside>
                                {CardPhoto(profile.cards[index])}
                                {CardQR(profile.cards[index])}
                                {CardHologram()}
                            </aside>

                        </>
        }

    </>);


    function CardLogo() {
        return <a className={"logo"} target={"_blank"} href={"https://www.ec2u.eu/"} title={"EC2U Alliance"}
            style={{ backgroundImage: `url('${page.icon}')` }}
        />;
    }

    function CardHome({

        hei: {

            name,
            home,
            logo

        }

    }: Card) {
        return <a className={"home"} target={"_blank"} href={home} title={name}
            style={{ backgroundImage: `url('${logo}')` }}
        />;
    }

    function CardMenu() {
        return <div className={"menu"}>

            {!profile ? <button title={"Loading"} className={"spin"}><RefreshCw/></button>

                : isError(profile) || !profile.user ? <button title={"Log in"} onClick={doLogIn}><LogIn/></button>

                    : <button title={"Log out"} onClick={doLogOut}><LogOut style={{ transform: "scaleX(-1)" }}/></button>
            }

            <button title={"About"} onClick={doFlip}><Info/></button>

            {!isError(profile) && profile?.cards && profile.cards.length > 1 && <>
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

        return <dl ref={data} className={"data"}>

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

        </dl>;
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