/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardPage } from "@ec2u/card/views/page";
import { immutable } from "@metreeca/core";
import React, { createElement } from "react";
import "./about.css";


export const about=immutable({

    id: "/about",
    label: "About"

});

export function CardAbout() {
    return <CardPage name={about}>{createElement("card-about", {}, <dl>

        <dt>Service</dt>

        <dd>

            <p>The EC2U virtual web-based Card Management System is the centralized application in charge of issuing and
                managing EC2U cards on behalf of all universities.</p>

            <p>All EC2U people can get their virtual cards by authenticating themselves using the personal eduGAIN
                credential provided by the university they come from.</p> <p>The card will contain a few personal
            information (such as the card holder name, the expiring date, the identifier, the card number, the EC2U
            hologram, the university logo and a QR-Code for integrity verification).</p>

            <p>Card holders can then show their own card to local university or public service providers in order to get
                the services they need. By scanning the QR-code, the local providers can verify the integrity and the
                validness of the card.</p>
        </dd>

        <dt>Users</dt>

        <dd>The EC2U virtual card is usable by all EC2U peoples: students, teachers or staff, as long as they are
            provided with eduGAIN credentials by their university.
        </dd>


        <dt>Management</dt>

        <dd> Each university can interact with the system via API or application in order to issue EC2U Cards.
            Implementing local adapters to interact with the Card Management System is left in charge of each university:
            each partner indeed with the support of the Consortium and the possible involvement of external suppliers
            will be responsible for implementing local procedures enabling their systems to receive and send data
            requests to the EC2U Card Management System according to the shared standards.
        </dd>

        <dt>Support</dt>

        <dd>[TBC]</dd>

        <dt>Privacy</dt>

        <dd>

            <p>No user data will be stored inside the EC2U Card Management System but just retrieved by two different
                repositories:</p>

            <dl>

                <dt>ESC Router</dt>
                <dd>this system is part of the EWP infrastructure, it holds several information about people involved
                    in a Erasmus process. Those information is retrieved by the EC2U Card Management System via API
                </dd>

                <dt>Local University Identity Management Systems</dt>
                <dd>this system is managed by the local universities and contains
                    information about the user identity, such as username and password as well as the European Student
                    Identifier. Such information is retrieved by the EC2U Card Management System through eduGAIN
                </dd>

            </dl>

            <p>These two repositories comply with the european’s general data protection regulation (GDPR)</p>

        </dd>

    </dl>)}</CardPage>;
}