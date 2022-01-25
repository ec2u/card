/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import * as React from "react";
import { render } from "react-dom";
import { CardHello } from "./hello";
import "./index.css";


render((<>

    <p>This app is app is saying hello to…</p>

    <CardHello name="Akhil"/>
    <CardHello name="Alessandro"/>

</>), document.body.firstElementChild);