/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

import * as React from "react";
import { render } from "react-dom";
import "./index.css";


/**
 * The absolute root URL with trailing slash.
 */
export const root=resolve(
    "/"
);

/**
 * The absolute base URL with trailing slash.
 */
export const base=resolve(".", resolve(
    import.meta.env.BASE_URL || document.querySelector("base")?.href || "", root
));


/**
 * The app name.
 */
export const name=document.title;

export const icon=(document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "";
export const copy=(document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || "";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

render(<>

    <img alt={"EC2U Connect Centre Logo"} src={icon}/>

</>, document.body.firstElementChild);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function resolve(path: string, base: string=location.href): string {
    return new URL(path, base).href;
}