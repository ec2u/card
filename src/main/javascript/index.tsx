/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardKeeper } from "@ec2u/card/nests/keeper";
import { CardRegister } from "@ec2u/card/nests/register";
import { CardRouter } from "@ec2u/card/nests/router";
import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


export const CardOptions={

    card: false,
    language: "en"

};

render((

    <React.StrictMode>

        {/*<CardFetcher>*/}
        <CardKeeper>

            <CardRegister state={CardOptions}>
                <CardRouter/>
            </CardRegister>

        </CardKeeper>
        {/*</CardFetcher>*/}

    </React.StrictMode>

), document.body.firstElementChild);

