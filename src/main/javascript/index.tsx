/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

import { CardMain } from "@ec2u/card/main";
import { CardGate } from "@ec2u/card/views/gate";
import { NodeFetcher, resolve } from "@metreeca/nest/fetcher";
import { NodeKeeper } from "@metreeca/nest/keeper";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";


/**
 * The absolute root URL with trailing slash.
 */
export const root=resolve("/");

/**
 * The absolute base URL with trailing slash.
 */
export const base=resolve(
    ".",
    resolve(import.meta.env.BASE_URL || document.querySelector("base")?.href || "", root)
);

/**
 * The app name.
 */
export const name=document.title;

export const icon=(document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "";
export const copy=(document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || "";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

