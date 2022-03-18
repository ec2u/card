/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import "@metreeca/skin/quicksand.css";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <a href={"/saml/login"}>login</a>

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

