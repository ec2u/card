/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { CardCard } from "@ec2u/card/views/card";
import { CardPage } from "@ec2u/card/views/page";
import * as React from "react";
import { render } from "react-dom";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { CardLocale } from "./hooks/locale";
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

        <CardLocale>
            <CardProfile>

                <BrowserRouter>
                    <Routes>

                        <Route path={"/"} element={<CardCard/>}/>
                        <Route path={"*"} element={<CardPage/>}/>

                    </Routes>
                </BrowserRouter>

            </CardProfile>
        </CardLocale>

    </React.StrictMode>

), document.body.appendChild(document.createElement("card-root")));
