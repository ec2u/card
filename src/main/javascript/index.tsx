/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import * as React from "react";
import { render } from "react-dom";
import { CardHello } from "./hello";
import "./index.css";


render((<>

    <p>This app is saying…</p>

    <CardHello name="Akhil" />
    <CardHello name="Andrea" />
    <CardHello name="Alessandro" />

</>), document.body.firstElementChild);