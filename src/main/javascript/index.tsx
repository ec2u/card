/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardMain } from "@ec2u/card/main";
import { CardKeeper } from "@ec2u/card/nests/keeper";
import { CardGate } from "@ec2u/card/views/gate";
import { NodeFetcher } from "@metreeca/nest/fetcher";
import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <NodeFetcher>
            <CardKeeper>

                <CardGate>

                    <CardMain/>

                </CardGate>

            </CardKeeper>
        </NodeFetcher>

    </React.StrictMode>

), document.body.firstElementChild);

