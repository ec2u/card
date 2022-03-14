/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { isDefined } from "@metreeca/core";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";
import { NodeKeeper } from "@metreeca/nest/keeper;


isDefined(null);
render((

    <React.StrictMode>

        <NodeFetcher>
            <NodeKeeper>

                <NodePage>
                    ciao!
                </NodePage>

                {/*<CardGate>

                 <CardMain/>

                 </CardGate>*/}

            </NodeKeeper>
        </NodeFetcher>

    </React.StrictMode>

), document.body.firstElementChild);

