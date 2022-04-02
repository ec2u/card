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

import { CardPage } from "@ec2u/card/views/page";
import React from "react";


export const About=Object.freeze({

    route: "/about",
    label: "About"

});

export function CardAbout() {
    return <CardPage name={About.label}>

        <h1>Service</h1>

        The EC2U Virtual Card is a lightweight mobile-based academic identification card built on top of two mature and
        widespread identity management systems:

        <ul>
            <li>the <a href={"https://europeanstudentcard.eu"} target={"_blank"}>European Student Card</a> (ESC)
                framework
            </li>
            <li>the <a href={"https://edugain.org"} target={"_blank"}>eduGAIN</a> federated identity network</li>
        </ul>

        <h1>Users</h1>

        <p>Faculty, students and staff registered with eduGAIN may be easily provided with a virtual card and
            access it using personal credentials issued by their home institution.</p>

        <p>Cardholders can demonstrate their academic affiliation and status by presenting their virtual card to local
            academic, public or commercial service providers.</p>

        <p>Service providers may verify card integrity and validity by scanning its QR code and automatically
            retrieve authoritative data about the card and its holder from to the central ESC server.</p>

        <h1>Management</h1>

        <p>Mobility operators at partner universities issue and manage virtual cards directly on the central
            ESC management system using their institutional accounts.</p>

        <p>Partner universities may opt to delegate the EC2U Connect Centre to operate on their behalf on the central
            ESC management system, in order to retrieve card details required to render virtual cards on mobile
            devices.</p>


        <h1>Support</h1>

        <p>Provided by the IT Services of the University of Pavia
            (<a href={"mailto:cc@ml.ec2u.eu"}>cc@ml.ec2u.eu</a>).
        </p>


        <h1>Privacy</h1>

        <p>The EC2U Virtual Card doesn't permanently store any personal cardholder information: all data
            required to render virtual cards are dynamically retrieved from central ESC facilities on the basis of a
            personal identification codes provided by the eduGAIN identity federation; both systems store personal
            information in compliance with the European General Data Protection Regulation (GDPR).</p>

    </CardPage>;
}