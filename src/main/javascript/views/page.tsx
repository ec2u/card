/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { root } from "@ec2u/card/pages/root";
import { Immutable, isArray, isDefined } from "@metreeca/core";
import { Value } from "@metreeca/core/value";
import { copy } from "@metreeca/head";
import { useProfile } from "@metreeca/nest/keeper";
import { CreditCard, Key, LogIn, LogOut, Users } from "@metreeca/skin/lucide";
import { NodeIcon } from "@metreeca/tile/widgets/icon";
import { NodePath } from "@metreeca/tile/widgets/path";
import React, { createElement, ReactNode } from "react";
import "./page.css";


export function CardPage({

    name=[],

    children

}: {

    name?: Value | ReactNode | Immutable<Array<Value | ReactNode>>

    children: ReactNode

}) {

    const [profile, setProfile]=useProfile();


    function doLogIn() {
        setProfile();
    }

    function doLogOut() {
        setProfile(null);
    }


    return createElement("card-page", {}, <>

        <aside>

            <header>
                <a href={"/"}><NodeIcon/></a>
            </header>

            <section>{profile ? <>

                <a href={"/cards/"}><CreditCard/></a>
                <a href={"/users/"}><Users/></a>
                <a href={"/keys/"}><Key/></a>

            </> : null}</section>

            <footer>{isDefined(profile)

                ? <button title={"Log Out"} onClick={doLogOut}><LogOut style={{ transform: "scaleX(-1)" }}/></button>
                : <button title={"Log In"} onClick={doLogIn}><LogIn/></button>

            }</footer>

        </aside>

        <main>

            <header>
                <NodePath>{isArray(name) ? [root, ...name] : [root, name]}</NodePath>
            </header>

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