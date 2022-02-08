/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import * as React from "react";
import { render } from "react-dom";


render((

    <p>This app is saying…</p>

    <CardHello name="Akhil" />
    <CardHello name="Andrea"/>
    <CardHello name="Luigi"/>
    <CardHello name="Alessandro"/>

</>), document.body.firstElementChild);