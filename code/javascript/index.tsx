/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

import { CardCard } from "@ec2u/card/views/card";
import { CardPage } from "@ec2u/card/views/page";
import * as React from "react";
import { render } from "react-dom";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { CardProfile } from "./hooks/profile";
import "./index.css";


/**
 * Page metadata.
 */
export const page=Object.freeze({

    /**
     * The URL of the page icon as read from the `<link rel="icon">` HTML head tag.
     */
    icon: (document.querySelector("link[rel='icon']") as HTMLLinkElement)?.href || "",

    /**
     * The page name as read from the `<title>` HTML head tag.
     */
    name: document.title.replace(/^EC2U\s+/, ""),

    /**
     * The page copyright as read from the `<meta name="copyright">` HTML head tag.
     */
    copy: (document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || ""

});


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

render((

    <React.StrictMode>

        <CardProfile>

            <BrowserRouter>
                <Routes>

                    <Route path={"/"} element={<CardCard/>}/>
                    <Route path={"*"} element={<CardPage/>}/>

                </Routes>
            </BrowserRouter>

        </CardProfile>

    </React.StrictMode>

), document.body.appendChild(document.createElement("card-root")));
