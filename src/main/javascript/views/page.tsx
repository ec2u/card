/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { isDefined } from "@metreeca/core";
import { copy, icon, name } from "@metreeca/head";
import { useKeeper } from "@metreeca/nest/keeper";
import { LogIn, LogOut } from "@metreeca/skin/lucide";
import React, { createElement, ReactNode } from "react";
import "./page.css";


export function CardPage({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useKeeper();


    function doLogIn() {
        setProfile(true);
    }

    function doLogOut() {
        setProfile(false);
    }


    return createElement("card-page", {}, <>

        <aside>

            <header>
                <button>
                    <i style={{ backgroundImage: `url('${icon}')` }}/>
                    {/*<span>{name}</span>*/}
                </button>
            </header>

            <section></section>

            <footer>{isDefined(profile)

                ? <button title={"Log Out"} onClick={doLogOut}><LogOut/></button>
                : <button title={"Log In"} onClick={doLogIn}><LogIn/></button>

            }</footer>

        </aside>

        <main>

            <header>{name}</header>
            <section>{children}</section>

            <footer>

                {copy}

                <a href={"/about"}>About</a>
                <a href={"/privacy"}>Privacy</a>
                <a href={"/contacts"}>Contacts</a>

            </footer>

        </main>

        </>
    );

}