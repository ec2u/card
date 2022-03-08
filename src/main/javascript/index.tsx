/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

import { CardMain } from "@ec2u/card/main";
import { CardGate } from "@ec2u/card/views/gate";
import { NodeFetcher } from "@metreeca/nest/fetcher";
import { NodeKeeper } from "@metreeca/nest/keeper";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <NodeFetcher>
            <NodeKeeper>

                <CardGate>

                    <CardMain/>

                </CardGate>

            </NodeKeeper>
        </NodeFetcher>

    </React.StrictMode>

), document.body.firstElementChild);

