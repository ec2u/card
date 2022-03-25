/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardOptions } from "@ec2u/card/index";
import { useOptions } from "@ec2u/card/nests/register";
import { CardPage } from "@ec2u/card/views/page";
import { useProfile } from "@metreeca/nest/keeper";
import { Contact, Landmark } from "@metreeca/skin/lucide";
import React, { createElement } from "react";


export function CardHome() {

    const [profile, setProfile]=useProfile();
    const [{ card }, setOptions]=useOptions<typeof CardOptions>();


    return <CardPage>{createElement("card-home", {}, <>

        <div><p><i title={"About EC2U"}><Landmark/></i> The European Campus of City-Universities (EC2U) is a
            multi-cultural and multi-lingual Alliance consisting
            of seven long-standing, education- and research-led, locally and globally engaged universities from four
            diverse
            regions of the European Union:</p>

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

        <div><p><i title={"About the EC2U Virtual Card"}><Contact/></i> The EC2U card is a tool for identifying Students
            and Staffs that are in a EC2U Mobility Project. By using the
            card, people can freely move through all contries getting all services and information they need to enjoy
            their amazing experience of mobility.</p>

            <p>If you joined a EC2U Mobility Project, then, get your eCard.</p>


        </div>

    </>)}

        {profile ? <button onClick={() => setOptions({ card: !card })} style={{

            display: "block",
            margin: "10% auto",
            transform: "scale(5)"

        }}><Contact/></button> : null}

    </CardPage>;
}