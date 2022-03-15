/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardCard } from "@ec2u/card/views/card";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


render((

    <React.StrictMode>

        <CardCard/>

        {/*<NodeFetcher>
         <NodeKeeper>

         <NodePage>
         ciao!
         </NodePage>

         <CardGate>

         <CardMain/>

         </CardGate>

         </NodeKeeper>
         </NodeFetcher>*/}

    </React.StrictMode>

), document.body.firstElementChild);

