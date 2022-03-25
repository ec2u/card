/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardPage } from "@ec2u/card/views/page";
import { immutable } from "@metreeca/core";
import { Globe, Landmark, Mail, MapPin } from "@metreeca/skin/lucide";
import React, { createElement } from "react";
import "./contacts.css";


export const contacts=immutable({

    id: "/contacts",
    label: "Contacts"

});

export function CardContacts() {
    return <CardPage name={contacts}>{createElement("card-contacts", {}, <dl>

        <dt title={"Name"}><Landmark/></dt>
        <dd>University of Pavia</dd>

        <dt title={"Address"}><MapPin/></dt>
        <dd>Via Ferrata 1, 27100 Pavia</dd>

        <dt title={"WWW"}><Globe/></dt>
        <dd><a href={"https://www.unipv.it/"}>www.unipv.it</a></dd>

        <dt title={"Mail"}><Mail/></dt>
        <dd><a href={"mailto:urp@unipv.it"}>urp@unipv.it</a></dd>

    </dl>)} </CardPage>;
}