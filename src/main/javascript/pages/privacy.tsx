/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardPage } from "@ec2u/card/views/page";
import { immutable } from "@metreeca/core";
import React, { createElement } from "react";
import "./privacy.css";


export const privacy=immutable({

    id: "/privacy",
    label: "Privacy"

});


export function CardPrivacy() {
    return <CardPage name={privacy.label}>{createElement("card-privacy", {}, <>

        <h1>Information on the Processing of Personal Data</h1>

        <small>pursuant to EU Regulation 2016/679 drawn up on 16/12/2021</small>

        <p>This document is intended to inform the interested party on how data concerning them is used in
            the context of the processing activity.</p>

        <table>
            <tbody>
                <tr>
                    <td><dfn>EC2U-CARD</dfn></td>
                    <td>EC2U-CARD Issue of personal card for mobility projects - Federation of identities</td>
                </tr>
            </tbody>
        </table>

        <p>Pursuant to articles 13 and 14 of EU regulation 2016/679, the interested party is informed that their data
            will be processed by the Data Controller defined in the <a href={"#subjects"}>Subjects</a> section, for
            the purpose defined in the section <a href={"#purpose"}>Purpose</a>, for a certain period of time defined
            in the <a href={"#retention"}>Retention Period</a> section and could be communicated to subjects identified
            in
            the <a href={"#diffusion"}>Diffusion</a> section.</p>

        <p>The interested party is also informed that with regard to their personal data they can exercise the right
            listed this information in the <a href={"#rights"}>Rights of the Interested Party </a> section. The rights
            of the interested party can be exercised at any time by contacting the Data Protection Officer (DPO) or,
            in his absence, the Data Controller.</p>


        <h2 id={"subjects"}>Subjects</h2>

        <small>Who processes my data and who can I contact for information and deal my rights?</small>

        <table>
            <thead>
                <tr>
                    <th>Qualification</th>
                    <th>Name</th>
                    <th>Contact details</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Holder</td>
                    <td><dfn>The University of Pavia</dfn></td>
                    <td>

                        <table>
                            <tbody>
                                <tr>
                                    <td>phone</td>
                                    <td>+39 0382 989898</td>
                                </tr>
                                <tr>
                                    <td>email</td>
                                    <td>
                                        <a href="mailto:amministrazione-centrale@certunipv.it">amministrazione-centrale@certunipv.it</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>address</td>
                                    <td>Strada Nuova, 65 - 27100 Pavia</td>
                                </tr>
                            </tbody>
                        </table>

                    </td>
                </tr>
                <tr>
                    <td>Responsible for data protection (RPD)</td>
                    <td><dfn>The University of Pavia</dfn></td>
                    <td>

                        <table>
                            <tbody>
                                <tr>
                                    <td>phone</td>
                                    <td>+39 0382 985490</td>
                                </tr>
                                <tr>
                                    <td>email</td>
                                    <td><a href="mailto:privacy@unipv.it">privacy@unipv.it</a></td>
                                </tr>
                                <tr>
                                    <td>address</td>
                                    <td>Strada Nuova, 65 - 27100 Pavia</td>
                                </tr>
                            </tbody>
                        </table>

                    </td>
                </tr>
            </tbody>
        </table>

        <h2 id={"purpose"}>Purpose</h2>

        <p>Why is data processed?</p>

        <table>
            <thead>
                <tr>
                    <th>Purpose of the treatment</th>
                    <th>Legal basis of the processing</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Student services</td>
                    <td>Free and informed consent</td>
                </tr>
                <tr>
                    <td>Student services</td>
                    <td>EC2U is an alliance of seven historical
                        and multidisciplinary European universities. The Universities that are part
                        of the alliance are the University of Coimbra (Portugal), Alexandru Ioan Cuza
                        of Iasi (Romania), the Friedrich Schiller University of Jena (Germany), Pavia
                        (Italy), Poitiers (Coordinator, France), Salamanca ( Spain) and Turku
                        (Finland). The EC2U Alliance has obtained the funding of an Erasmus +
                        Strategic Partnership 'European Digital University Card Student - EDUcardS'
                        for the adoption of the European Student Card (ESC) within the alliance,
                        which it can offer to all students of the universities involved access to the
                        services provided for those of the headquarters (canteen, sports services,
                        public transport, etc.).
                    </td>
                </tr>
            </tbody>
        </table>

        <h2 id={"diffusion"}>Diffusion</h2>

        <table>
            <thead>
                <tr>
                    <th>Does processing involves diffusion of personal data?</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>No</td>
                </tr>
            </tbody>
        </table>

        <h2 id={"retention"}>Retention Period</h2>

        <table>
            <thead>
                <tr>
                    <th>Data retention period or criterion for determining it</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Necessity - Based on the principle of necessity of the treatment in
                        relation to the purpose<br/>Description: Data about end users
                        of the service are not stored. The card is displayed in real
                        time and not stored by the system
                    </td>
                </tr>
            </tbody>
        </table>

        <h2 id={"origin"}>Origin of Data</h2>

        <table>
            <thead>
                <tr>
                    <th>Place of data collection</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Collection from the interested party</td>
                </tr>
            </tbody>
        </table>

        <h2 id={"categories"}>Categories of data processed</h2>

        <table>
            <thead>
                <tr>
                    <th>Categories of Data</th>
                    <th>Data Detail</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Personal data</td>
                    <td>Name, Surname, Tax Code</td>
                </tr>
            </tbody>
        </table>

        <h2 id={"automation"}>Automated Decision Making and Profiling</h2>

        <small>Is data concerning me used to profile me? Are decisions made automatically on the basis of the
            profile?</small>

        <table>
            <thead>
                <tr>
                    <th>Profiling</th>
                    <th>Automated Decision Making</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>No</td>
                    <td>No</td>
                </tr>
            </tbody>
        </table>

        <h2 id={"rights"}>Rights of the Interested Party</h2>

        <table>
            <thead>
                <tr>
                    <th>Right</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Access</td>
                    <td>Access to personal data concerning them</td>
                </tr>
                <tr>
                    <td>Portability</td>
                    <td>Receive in a structured format, commonly used and readable by an automatic device
                    </td>
                </tr>
                <tr>
                    <td>Rectification</td>
                    <td>The rectification of personal data concerning them</td>
                </tr>
                <tr>
                    <td>Oblivion</td>
                    <td>The deletion of personal data concerning them</td>
                </tr>
                <tr>
                    <td>Limitation</td>
                    <td>The limitation of the processing of personal data concerning them</td>
                </tr>
                <tr>
                    <td>Opposition</td>
                    <td>Opposition to processing for certain purposes</td>
                </tr>
            </tbody>
        </table>

        <p>The interested party can also withdraw the expressed consent at any time without prejudice to the lawfulness
            of the treatment based on the consent given before the revocation. </p>

        <p>To exercise the aforementioned rights, the interested party can contact the Data Protection Officer or the
            Holder.</p>

        <p>The interested party has the right to lodge a complaint with a supervisory authority, by writing
            to <a href="mailto:garante@gpdp.it">garante@gpdp.it</a>, or
            to <a href="mailto: protocol@pec.gpdp.it ">protocol@pec.gpdp.it</a>.</p>

    </>)}</CardPage>;

}