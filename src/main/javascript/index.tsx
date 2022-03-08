/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { NodeFetcher } from "@metreeca/nest/fetcher";
import { NodeKeeper } from "@metreeca/nest/keeper";
import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <NodeFetcher>
            <NodeKeeper>

                ciao babbo!

                {/*<CardGate>

                 <CardMain/>

                 </CardGate>*/}

            </NodeKeeper>
        </NodeFetcher>

    </React.StrictMode>

), document.body.firstElementChild);

