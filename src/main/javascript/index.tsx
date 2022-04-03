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

import { About, CardAbout } from "@ec2u/card/pages/about";
import { CardContacts, Contacts } from "@ec2u/card/pages/contacts";
import { CardPrivacy, Privacy } from "@ec2u/card/pages/privacy";
import { CardCard } from "@ec2u/card/views/card";
import { User } from "lucide-react";
import * as React from "react";
import { render } from "react-dom";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import "./index.css";


export interface Profile {

    readonly manager: string;
    readonly version: string;
    readonly instant: string;

    readonly user?: User;
    readonly cards?: Card[];

}

export interface User {

    readonly esi: string;
    readonly uni: string;

}

export interface Card {

    readonly code: string;
    readonly test: string;
    readonly expiry: string;

    readonly esi: string;
    readonly level: number;
    readonly name: string;
    readonly photo?: string;

    readonly hei: {

        readonly pic: number;
        readonly name: string;

        readonly iso: string;
        readonly country: string;

    };

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export const home="https://www.ec2u.eu/"; // !!! from profile.manager

/**
 * The app name as read from the `<title>` HTML head tag.
 */
export const name: string=document.title;

/**
 * The URL of the app icon as read from the `<link rel="icon">` HTML head tag.
 */
export const icon: string=(document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "";

/**
 * The app copyright as read from the `<meta name="copyright">` HTML head tag.
 */
export const copy=(document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || "";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

render((

    <React.StrictMode>

        <BrowserRouter>
            <Routes>

                <Route path={"/"} element={<CardCard/>}/>

                <Route path={About.route} element={<CardAbout/>}/>
                <Route path={Privacy.route} element={<CardPrivacy/>}/>
                <Route path={Contacts.route} element={<CardContacts/>}/>

                <Route path="*" element={<Navigate replace to="/"/>}/>

            </Routes>
        </BrowserRouter>

    </React.StrictMode>

), document.body.firstElementChild);
