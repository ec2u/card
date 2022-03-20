/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardFetcher } from "@ec2u/card/nests/fetcher";
import { CardKeeper } from "@ec2u/card/nests/keeper";
import { CardRouter } from "@ec2u/card/nests/router";
import { CardGate } from "@ec2u/card/views/gate";
import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <CardFetcher>
            <CardKeeper>

                <CardGate>
                    <CardRouter/>
                </CardGate>

            </CardKeeper>
        </CardFetcher>

    </React.StrictMode>

), document.body.firstElementChild);

