/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardOptions } from "@ec2u/card/index";
import { useOptions } from "@ec2u/card/nests/register";
import { CardPage } from "@ec2u/card/views/page";
import { useProfile } from "@metreeca/nest/keeper";
import { Contact, Globe, Landmark, LogIn } from "@metreeca/skin/lucide";
import React, { createElement } from "react";
import "./home.css";


export function CardHome() {

    const [profile, setProfile]=useProfile();
    const [{ card }, setOptions]=useOptions<typeof CardOptions>();


    function doLogIn() {
        setProfile();
    }


    return <CardPage>{createElement("card-home", {}, <>

        <div>

            <i title={"About EC2U"}><Landmark/></i>

            <p>The <a target={"_blank"} href={"https://www.ec2u.eu/"}>EC2U European Campus of City‑Universities</a> is
                a multi-cultural and multi-lingual Alliance consisting of seven long-standing, education and
                research-led, locally and globally engaged universities from four diverse regions of the
                European Union:</p>

            <ul>
                <li>University of Coimbra - Portugal</li>
                <li>Alexandru Ioan Cuza University of Iasi - Romania</li>
                <li>University of Jena - Germany</li>
                <li>University of Pavia - Italy</li>
                <li>University of Poitiers - France (Coordinator)</li>
                <li>University of Salamanca - Spain</li>
                <li>University of Turku - Finland</li>
            </ul>

        </div>

        <div>

            <i title={"About the EC2U Virtual Card"}><Contact/></i>

            <p>The EC2U Virtual Card identifies member faculty, students and staff. Using the card, people can
                freely move within the Alliance and easily access local services and resources.</p>

            <i title={"Sign up for a EC2U Virtual Card"}><Globe/></i>

            <p>Contact your local International Mobility office to register your new EC2U Virtual Card…</p>

            <i title={"Access your EC2U Virtual Card"}><LogIn/></i>

            <p>… or <a onClick={doLogIn}>authenticate</a> with your local identity service to access your EC2U Virtual
                Card</p>

        </div>

    </>)}

        {profile ? <button onClick={() => setOptions({ card: !card })} style={{

            display: "block",
            margin: "10% auto",
            transform: "scale(5)"

        }}><Contact/></button> : null}

    </CardPage>;
}