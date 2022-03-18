/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <ul>
            <li><a href={"/saml/login"}>login</a></li>
            <li><a href={"/saml/discover"}>discover</a></li>
        </ul>


        {/*
         <NodeFetcher>
         <CardKeeper>

         <CardGate>
         <CardRouter/>
         </CardGate>

         </CardKeeper>
         </NodeFetcher>
         */}

    </React.StrictMode>

), document.body.firstElementChild);

