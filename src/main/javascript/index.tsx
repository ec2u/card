/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardKeeper } from "@ec2u/card/nests/keeper";
import { CardRouter } from "@ec2u/card/nests/router";
import { NodeFetcher } from "@metreeca/nest/fetcher";
import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <NodeFetcher>
            <CardKeeper>

                {/*<CardGate>*/}
                <CardRouter/>
                {/*</CardGate>*/}

            </CardKeeper>
        </NodeFetcher>

    </React.StrictMode>

), document.body.firstElementChild);

