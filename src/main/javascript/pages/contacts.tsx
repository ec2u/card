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


export const Contacts=Object.freeze({

    route: "/contacts",
    label: "Contacts"

});

export function CardContacts() {

    return <CardPage name={Contacts.label}>

        <p>
            EC2U Alliance<br/>
            c/o University of Pavia<br/>
            Via Ferrata 1<br/>
            27100 Pavia<br/>
            Italy
        </p>


        <p>
            <a href={"https://www.ec2u.eu"}>www.ec2u.eu</a><br/>
            <a href={"mailto:cc@ml.ec2u.eu"}>cc@ml.ec2u.eu</a>
        </p>

    </CardPage>;
}