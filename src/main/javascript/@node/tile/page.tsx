/*
 * Copyright © 2020-2022 Metreeca srl
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

import { icon } from "@metreeca/tile/index";
import React, { createElement, ReactNode } from "react";
import "./page.css";


export interface Pane {

    icon: ReactNode;
    label: string;

    head?: ReactNode;
    body?: ReactNode;
    foot?: ReactNode;

}

export function NodePage({

    children

}: {

    children: ReactNode

}) {

    return createElement("node-page", {}, <>

        <nav>
            <header>
                <button style={{ backgroundImage: `url(${icon})` }}/>
            </header>
            <section></section>
            <footer>user</footer>
        </nav>

        <aside>
            <header></header>
            <section></section>
            <footer></footer>
        </aside>

        <main>
            <header>
                <button style={{ backgroundImage: `url(${icon})` }}/>
                <span>name</span>
                <nav>menu</nav>
            </header>
            <section>{children}</section>
            <footer>copy</footer>
        </main>

    </>);

}