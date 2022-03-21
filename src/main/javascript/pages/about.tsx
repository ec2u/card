/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardPage } from "@ec2u/card/views/page";
import React from "react";


export function CardAbout() {
    return <CardPage>

        <p>The European Campus of City-Universities (EC2U) is a multi-cultural and multi-lingual Alliance consisting of
            seven long-standing, education- and research-led, locally and globally engaged universities from four diverse
            regions of the European Union: the University of Coimbra (Portugal), Alexandru Ioan Cuza University of Iasi
            (Romania), the University of Jena (Germany), the University of Pavia (Italy), the University of Poitiers
            (France-Coordinator), the University of Salamanca (Spain) and the University of Turku (Finland).</p>

        <p>The EC2U card is a tool for identifying Students and Staffs that are in a EC2U Mobility Project. By using the
            card, people can freely move through all contries getting all services and information they need to enjoy
            their amazing experience of mobility.</p>

        <p>If you joined a EC2U Mobility Project, then, get your eCard.</p>

        <p>Service Description
            The EC2U virtual web-based Card Management System is the centralized application in charge of issuing and
            managing EC2U cards on behalf of all universities.
            All EC2U people can get their virtual cards by authenticating themselves using the personal eduGain
            credential provided by the university they come from. The card will contain a few personal information (such
            as the card holder name, the expiring date, the identifier, the card number, the EC2U hologram, the
            university logo and a QR-Code for integrity verification). The card holders can then show their own card to
            the local public service providers in order to get the services they need. By scanning the QR-code, the local
            providers can verify the integrity and the validness of the card. The virtual card can be downloaded using
            either desktop and mobile devices and in this latter case, depending on the used operating system, the card
            can be stored inside a native wallet app, to keep it safe.


            Users
            The EC2U virtual card is usable by all EC2U peoples: students, teachers or staff, as long as they are
            provided with eduGain credentials by their university.


            Management
            Each university can interact with the system via API or application in order to issue EC2U Cards.
            Implementing local adapters to interact with the Card Management System is left in charge of each university:
            each partner indeed with the support of the Consortium and the possible involvement of external suppliers
            will be responsible for implementing local procedures enabling their systems to receive and send data
            requests to the EC2U Card Management System according to the shared standards.


            Support


            Privacy
            No user data will be stored inside the EC2U Card Management System but just retrieved by two different
            repositories:

            ESC Router: this system is part of the EWP infrastructure, it holds several information about people involved
            in a Erasmus process. Those information is retrieved by the EC2U Card Management System via API;
            Local University Identity Management System: this system is managed by the local universities and contains
            information about the user identity, such as username and password as well as the European Student
            Identifier. Such information is retrieved by the EC2U Card Management System through eduGain.
            These two repositories comply with the european’s general data protection regulation (GDPR).
        </p>

    </CardPage>;
}