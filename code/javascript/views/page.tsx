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

import Slugger from "github-slugger";
import { Root } from "hast";
import { headingRank } from "hast-util-heading-rank";
import { toString } from "hast-util-to-string";
import "highlight.js/styles/github.css";
import { Menu, RefreshCw, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import ReactMarkdown, { uriTransformer } from "react-markdown";
import { NavLink, useLocation } from "react-router-dom";
import rehypeHighlight from "rehype-highlight";
import rehypeSlug from "rehype-slug";
import deflist from "remark-deflist";
import remarkFrontmatter from "remark-frontmatter";
import remarkGemoji from "remark-gemoji";
import remarkGfm from "remark-gfm";
import "./page.css";


/**
 * Page metadata.
 */
const page=Object.freeze({

    home: "https://www.ec2u.eu/",

    /**
     * The URL of the page icon as read from the `<link rel="icon">` HTML head tag.
     */
    icon: (document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "",

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

export function CardPage() {

    const location=useLocation();
    const [menu, setMenu]=useState(false);

    const [status, setStatus]=useState<number>();
    const [content, setContent]=useState<string>();


    const path=location.pathname;
    const hash=location.hash;


    useEffect(() => {

        setStatus(undefined);
        setContent(undefined);

        if ( path.match(/^\/([-\w]+\/)*([-\w]*)$/) ) { // path is route

            fetch(`${path}${path.endsWith("/") ? "index.md" : ".md"}`)

                .then(response => response.text().then(content => {

                    setStatus(response.status);
                    setContent(response.ok ? content : undefined);

                }))

                .then(() => {

                    document.getElementById(hash.substring(1))?.scrollIntoView(); // scroll to anchor

                })

                .catch(() => {});

        }

    }, [path]);



    return createElement("card-page", {

        class: menu ? "nav" : "main"

    }, <>

        <nav onClick={e => {

            if ( (e.target as Element).tagName === "A" ) { setMenu(false);}

        }}>

            <header>

                <a href={page.home} target={"_blank"} style={{ backgroundImage: `url('${page.icon}')` }}/>

                <a href={"/"}>{page.name}</a>

                {menu
                    ? <button onClick={() => setMenu(false)}><X/></button>
                    : <button onClick={() => setMenu(true)}><Menu/></button>
                }

            </header>

            <section>

                <NavLink to={"/about"}>About</NavLink>
                <NavLink to={"/privacy"}>Privacy</NavLink>
                <NavLink to={"/contacts"}>Contacts</NavLink>

                {/*{<footer>{meta}</footer>}*/}


            </section>

            <section>{content &&

                <ReactMarkdown

                    remarkPlugins={[remarkFrontmatter]}
                    rehypePlugins={[rehypeTOC]}

                >{

                    content

                }</ReactMarkdown>

            }</section>

        </nav>

        <main {...(!menu ? { className: "active" } : {})}>

            <header>{content ? content.match(/^title:\s*(.*)$/m)?.[1]
                : status === undefined ? <RefreshCw/>
                    : status === 404 ? ":-( Page Not Found"
                        : status/100 >= 4 ? `:-( The Server Says ${status}…`
                            : null
            }</header>

            <section>{content &&

                <ReactMarkdown

                    remarkPlugins={[remarkFrontmatter, remarkGfm, remarkGemoji, deflist]}
                    rehypePlugins={[rehypeSlug, rehypeHighlight]}

                    transformLinkUri={href => [uriTransformer(href)]
                        .map(value => value.endsWith("/index.md") ? value.substring(0, value.length-"/index.md".length) : value)
                        .map(value => value.endsWith(".md") ? value.substring(0, value.length-".md".length) : value)
                        [0]
                    }

                >{

                    content

                }</ReactMarkdown>

            }</section>

            <footer>{page.copy}</footer>

        </main>

    </>);

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


function rehypeTOC() {
    return (root: Root) => {

        const slugs=new Slugger();

        slugs.reset();

        return {

            ...root, children: (root.children).filter((node) => headingRank(node)).map(node => ({
                ...node, children: [{
                    ...node,
                    type: "element",
                    tagName: "a",
                    properties: { href: `#${slugs.slug(toString(node))}` }
                }]
            }))

        };

    };
}

