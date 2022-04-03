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

import { useProfile } from "@ec2u/card/hooks/profile";
import { copy, Profile } from "@ec2u/card/index";
import { About } from "@ec2u/card/pages/about";
import { Contacts } from "@ec2u/card/pages/contacts";
import { Privacy } from "@ec2u/card/pages/privacy";
import { CardIcon } from "@ec2u/card/views/icon";
import { Menu, X } from "lucide-react";
import React, { createElement, ReactNode, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import "./page.css";


export function CardPage({

    name,

    children

}: {

    name: string

    children: ReactNode

}) {

    const [profile]=useProfile();
    const [menu, setMenu]=useState(false);


    function version({ version, instant }: Profile) {

        const date=new Date(instant);

        const year=date.getFullYear().toString();
        const month=(date.getMonth()+1).toString().padStart(2, "0");
        const day=date.getDate().toString().padStart(2, "0");

        return `v${version}+${year}${month}${day}`;
    }


    return createElement("card-page", {}, <>

        <header>

            <a href={profile?.manager || "/"} target={"_blank"}><CardIcon/></a>

            <Link to={"/"}>Virtual Card</Link>

            {menu
                ? <button onClick={() => setMenu(false)}><X/></button>
                : <button onClick={() => setMenu(true)}><Menu/></button>
            }


        </header>

        <nav {...(menu ? { className: "active" } : {})}>

            <NavLink to={About.route}>{About.label}</NavLink>
            <NavLink to={Privacy.route}>{Privacy.label}</NavLink>
            <NavLink to={Contacts.route}>{Contacts.label}</NavLink>

            <footer>{profile && version(profile)}</footer>

        </nav>

        <main {...(!menu ? { className: "active" } : {})}>

            <header>{name}</header>

            <section>{children}</section>

            <footer>{copy}</footer>

        </main>

    </>);

}