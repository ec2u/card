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


export const Privacy=Object.freeze({

    route: "/privacy",
    label: "Privacy"

});

export function CardPrivacy() {
    return <CardPage name={Privacy.label}>

        <h1>Information on the Processing of Personal Data</h1>

        <blockquote>Pursuant to EU Regulation 2016/679 drawn up on 16/12/2021</blockquote>

        <p>This document is intended to inform the interested party on how data concerning them is used in
            the context of the following processing activities:</p>

        <table>
            <thead>
                <tr>
                    <th>Service</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><strong><dfn>EC2U-CARD</dfn></strong></td>
                    <td>Issue of personal card for mobility projects - Federation of identities</td>
                </tr>
            </tbody>
        </table>

        <p>Pursuant to articles 13 and 14 of EU regulation 2016/679, the interested party is informed that their
            data
            will be processed by the Data Controller defined in the <a href={"#subjects"}>Subjects</a> section,
            for the
            purpose defined in the <a href={"#purpose"}>Purpose</a> section, for a period of time defined in
            the <a href={"#retention"}>Retention Period</a> section, and could be communicated to subjects
            identified in
            the <a href={"#diffusion"}>Diffusion</a> section.</p>

        <p>The interested party is also informed that with regard to their personal data they can exercise the
            right
            listed this information in the <a href={"#rights"}>Rights of the Interested Party</a> section. The
            rights
            of the interested party can be exercised at any time by contacting the Data Protection Officer (DPO)
            or,
            in their absence, the Data Controller.</p>


        <h2 id={"subjects"}>Subjects</h2>

        <blockquote>Who processes my data and who can I contact for information and deal my rights?</blockquote>

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
                    <td><dfn>University of Pavia</dfn></td>
                    <td>
                        Strada Nuova, 65 - 27100 Pavia<br/>
                        +39 0382 989898<br/>
                        <a href="mailto:amministrazione-centrale@certunipv.it">amministrazione-centrale@certunipv.it</a>
                    </td>
                </tr>
                <tr>
                    <td>Responsible for data protection (RPD)</td>
                    <td><dfn>University of Pavia</dfn></td>
                    <td>
                        Strada Nuova, 65 - 27100 Pavia<br/>
                        +39 0382 985490<br/>
                        <a href="mailto:privacy@unipv.it">privacy@unipv.it</a>
                    </td>
                </tr>
            </tbody>
        </table>

        <h2 id={"purpose"}>Purpose</h2>

        <blockquote>Why is data processed?</blockquote>

        <table>
            <thead>
                <tr>
                    <th>Purpose of the treatment</th>
                    <th>Legal basis of the processing</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td width={"33%"}>Student and staff services</td>
                    <td>Free and informed consent</td>
                </tr>
                <tr>
                    <td>Mobility card</td>
                    <td>The EC2U Alliance has obtained the funding of an Erasmus +
                        Strategic Partnership 'European Digital University Card Student - EDUcardS'
                        for the adoption of the European Student Card (ESC) within the alliance,
                        which it can offer to all students of the universities involved access to the
                        services provided for those of the headquarters (canteen, sports services,
                        public transport,&nbsp;…).
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
                        relation to the purpose; data about end users are not stored permanently by the service
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

        <h2 id={"categories"}>Categories of Data Processed</h2>

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
                    <td>Name, surname, email address, personal identification codes</td>
                </tr>
            </tbody>
        </table>

        <h2 id={"automation"}>Automated Decision Making and Profiling</h2>

        <blockquote>
            Is data concerning me used to profile me?<br/>
            Are decisions made automatically on the basis of the profile?
        </blockquote>

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

        <p>The interested party can also withdraw the expressed consent at any time without prejudice to the
            lawfulness
            of the treatment based on the consent given before the revocation.</p>

        <p>To exercise the aforementioned rights, the interested party can contact the Data Protection Officer or
            the
            Holder.</p>

        <p>The interested party has the right to lodge a complaint with a supervisory authority, by writing
            to <a href="mailto:garante@gpdp.it">garante@gpdp.it</a>, or
            to <a href="mailto: protocol@pec.gpdp.it ">protocol@pec.gpdp.it</a>.</p>

    </CardPage>;
}