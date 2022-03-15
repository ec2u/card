/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { CardMain } from "@ec2u/card/main";
import { CardGate } from "@ec2u/card/views/gate";
import * as React from "react";
import { render } from "react-dom";
import "./index.css";
import { NodeFetcher } from "./nests/fetcher";
import { NodeKeeper } from "./nests/keeper";


/**
 * The app name as read from the `<title>` HTM head tag.
 */
export const name: string=document.title?.replace(/^EC2U\s+/, "") ?? "";

/**
 * The URL of the app icon as read from the `<link rel="icon">` HTML head tag.
 */
export const icon: string=(document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "";


/**
 * The app copyright as read from the `<meta name="copyright">` HTML head tag.
 */
export const copy=(document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || "";


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

