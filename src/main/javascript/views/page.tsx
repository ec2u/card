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

import { page } from "@ec2u/card/index";
import { CardIcon } from "@ec2u/card/views/icon";
import { CardSpin } from "@ec2u/card/views/spin";
import { Menu, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import { Link, NavLink, useLocation } from "react-router-dom";
import rehypeSlug from "rehype-slug";
import remarkFrontmatter from "remark-frontmatter";
import remarkGemoji from "remark-gemoji";
import remarkGfm from "remark-gfm";
import "./page.css";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardPage() {

    const [navigate, setNavigate]=useState(false);

    const [status, setStatus]=useState<number>();
    const [content, setContent]=useState<string>();

    const path=useLocation().pathname;

    useEffect(() => {

        setStatus(undefined);
        setContent(undefined);

        if ( path.match(/^\/([-.\w]+\/)*([-.\w]*)$/) ) { // no user content and path is route

            fetch(`${path}${path.endsWith("/") ? "index.md" : ".md"}`)

                .then(response => response.text().then(content => {

                    setStatus(response.status);
                    setContent(content);

                }))

                .catch(() => {});
        }


    }, [path]);

    return createElement("card-page", {}, <>

        <header>

            <a href={"https://www.ec2u.eu/"} target={"_blank"}><CardIcon/></a>

            <Link to={"/"}>{page.name}</Link>

            {navigate
                ? <button onClick={() => setNavigate(false)}><X/></button>
                : <button onClick={() => setNavigate(true)}><Menu/></button>
            }

        </header>

        <nav {...(navigate ? { className: "active" } : {})} onClick={e => {

            if ( (e.target as Element).tagName === "A" ) { setNavigate(false); }

        }}>

            <NavLink to={"/about"}>About</NavLink>
            <NavLink to={"/privacy"}>Privacy</NavLink>
            <NavLink to={"/contacts"}>Contacts</NavLink>

            {/*{<footer>{meta}</footer>}*/}

        </nav>

        <main {...(!navigate ? { className: "active" } : {})}>

            <header>{status === undefined ? <CardSpin/>
                : status === 404 ? ":-( Page Not Found"
                    : status/100 >= 4 ? `:-( The Server Says ${status}…`
                        : content?.match(/^\s*title:\s*(.*)$/m)?.[1]
            }</header>

            <section>
                {content && <ReactMarkdown

                    remarkPlugins={[remarkFrontmatter, remarkGfm, remarkGemoji]}
                    rehypePlugins={[rehypeSlug]}

                >{content}</ReactMarkdown>}
            </section>

            <footer>{page.copy}</footer>

        </main>

    </>);

}
