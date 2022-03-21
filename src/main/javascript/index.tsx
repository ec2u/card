/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardFetcher } from "@ec2u/card/nests/fetcher";
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

        <CardRegister state={CardOptions}>

            <CardFetcher>
                <CardKeeper>

                    <CardRouter/>

                </CardKeeper>
            </CardFetcher>

        </CardRegister>

    </React.StrictMode>

), document.body.firstElementChild);

