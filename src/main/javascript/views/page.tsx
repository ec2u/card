/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { icon, name } from "@metreeca/head";
import React, { createElement, ReactNode } from "react";
import "./page.css";


export function CardPage({

    children

}: {

    children: ReactNode

}) {

    return createElement("card-page", {}, <>

            <aside>

                <header>
                    <button>
                        <i style={{ backgroundImage: `url('${icon}')` }}/>
                        <span>{name}</span>
                    </button>
                </header>

                <section></section>
                <footer></footer>

            </aside>

            <main>

                <header>European Campus of City-Universities</header>
                <section>{children}</section>
                <footer></footer>

            </main>

        </>
    );

}