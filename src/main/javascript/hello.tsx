/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import * as React from "react";
import { createElement } from "react";
import "./hello.css";


export function CardHello({

    name

}: {

    name: string

}) {

    return createElement("card-hello", {}, <>

        Hello <span>{name}</span>!

    </>);

}