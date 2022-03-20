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

import { base, name, root } from "@metreeca/head/index";
import { createContext, createElement, PropsWithChildren, ReactElement, useCallback, useContext, useEffect, useReducer } from "react";


/**
 * Router context.

 * @module
 */

const ActiveAttribute="active";
const NativeAttribute="native";
const TargetAttribute="target";

const Context=createContext<{ store: Store, update: () => void }>({ store: path(), update: () => {} });


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The value component of the router context state.
 */
export type Value=string

/**
 * The updater component of the router context state.
 */
export interface Updater {

    (): string;

    (route: string | { route?: string, state?: any, label?: string }, replace?: boolean): void;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Creates an attribute spread for active links.
 *
 * @param route the target link route; may include a trailing `*` to match nested routes
 *
 * @return an attribute spread including an `href` attribute for `route` and an optional `active` boolean
 * attribute if `route` matches the current route
 */
export function active(route: string): { href: string, [ActiveAttribute]?: "" } {

    const wild=route.endsWith("*");

    const href=wild ? route.substr(0, route.length-1) : route;

    function matches(target: string, current: string) {
        return wild ? current.startsWith(target) : current === target;
    }

    return { href: href, [ActiveAttribute]: matches(href, useContext(Context).store()) ? "" : undefined };

}

/**
 * Creates an attribute spread for native links.
 *
 * @param route the target link route
 *
 * @return an attribute spread including an `href` attribute for `route` and an `native` boolean attribute
 */
export function native(route: string): { href: string, [NativeAttribute]?: "" } {
    return { href: route, [NativeAttribute]: "" };
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Route store.
 */
export interface Store {

    /**
     * Converts browser location to a route.
     *
     * @returns the current route as extracted from the current browser location
     */
    (): Value;

    /**
     * Converts a route to a browser location.
     *
     * @param route the route to be converted
     *
     * @returns a location-relative string representing `route`
     */
    (route: Value): string;

}


/**
 * Creates a path {@link Store route store}
 *
 * @return a function managing routes as relative-relative paths including search and hash
 */
export function path(): Store {
    return (route?: string) => route === undefined

        ? location.href.startsWith(base) ? location.pathname : location.href

        : route ? `${base}${route.startsWith("/") ? route.substr(1) : route}` : location.pathname;

}

/**
 * Creates a hash {@link Store route store}
 *
 * @return a function managing routes as hashes
 */
export function hash(): Store {
    return (route?: string) => route === undefined

        ? location.hash.substring(1)

        : route ? route.startsWith("#") ? route : `#${route}` : location.hash;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Routing switch.
 */
export interface Switch {

    /**
     * Retrieves a component responsible for rendering a route.
     *
     * @param route the route to be rendered
     *
     * @returns a a rendering of `route` or a new route if a redirection is required
     */
    (route: string): undefined | null | string | ReactElement;

}

/**
 * Routing table.
 */
export interface Table {

    /**
     * Maps glob patterns either to components or redirection patterns.
     *
     * Patterns may include the following wildcards, where `step` is a sequence of word chars:
     *
     * - `{step}` matches a non empty named path step
     * - `{}` matches a non-empty anonymous path step
     * - `/*` matches a trailing path
     *
     * Redirection patterns may refer to wildcards in the matched route pattern as:
     *
     * - `{step}` replaced with the matched non empty named path step
     * - `{}` replaced with the whole matched route
     * - `/*` replaced with the matched trailing path
     *
     * Named path step in the matched route pattern are also included in the `props` argument of the component as:
     *
     * - `step` the matched non empty named path step
     * - `$` the matched trailing path
     */
    readonly [pattern: string]: string | Entry;

}

/**
 * Routing table entry.
 */
export interface Entry {

    (props: PropsWithChildren<any>, context?: any): undefined | null | ReactElement;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function NodeRouter({

    store=path(),

    routes

}: {

    /**
     * The route store
     *
     * @default {@link path()}
     */
    store?: Store

    routes: Table | Switch

}) {

    const table=routes instanceof Function ? routes : compile(routes);


    const update=useReducer(v => v+1, 0)[1];


    const click=useCallback((e: MouseEvent) => {

        if ( !(e.altKey || e.ctrlKey || e.metaKey || e.shiftKey || e.defaultPrevented) ) { // only plain events

            const anchor=(e.target as Element).closest("a");

            const native=anchor?.getAttribute(NativeAttribute);
            const target=anchor?.getAttribute(TargetAttribute);

            if ( anchor
                && native === null // only non-native anchors
                && (target === null || target === "_self") // only local anchors
            ) {

                const href=anchor.href;
                const file="file:///";

                const route=href.startsWith(base) ? href.substr(base.length-1)
                    : href.startsWith(root) ? href.substr(root.length-1)
                        : href.startsWith(file) ? href.substr(file.length-1)
                            : "";

                if ( route ) { // only internal routes

                    e.preventDefault();

                    try {

                        history.pushState(undefined, document.title, store(route));

                    } finally {

                        update();

                    }

                }
            }
        }

    }, []);


    useEffect(() => {

        window.addEventListener("popstate", update);
        window.addEventListener("click", click);

        return () => {
            window.removeEventListener("popstate", update);
            window.removeEventListener("click", click);
        };

    }, []);


    return createElement(Context.Provider, {

        value: { store, update }

    }, createElement(function () {

        let current=store();

        const redirects=new Set([current]);

        while ( true ) {

            const component=table(current);

            if ( typeof component === "string" ) {

                if ( redirects.has(component) ) {

                    console.error(`redirection loop <${Array.from(redirects)}>`);

                    return null;

                }

                redirects.add(current=component);

            } else { // ;( no useEffect() / history must be updated before component is rendered

                history.replaceState(history.state, document.title, store(current)+location.search);

                return component || null; // ;(react) undefined is not allowed

            }

        }

    }));

}

export function useRoute(): [Value, Updater] {

    const { store, update }=useContext(Context);

    return [store(), (entry, replace) => {

        if ( entry === undefined ) {

            return store();

        } else {

            const { route, state, label }=(typeof entry === "string")
                ? { route: entry, state: undefined, label: undefined }
                : entry;

            const _route=(route === undefined) ? location.href : (route === null) ? location.origin : /*absolute*/(store(route));
            const _state=(state === undefined) ? history.state : (state === null) ? undefined : state;
            const _label=(label === undefined) ? document.title : label && name ? `${label} | ${name}` : label || name;

            const modified=_route !== location.href || _state !== history.state /*|| _label !== document.title*/;


            try {

                if ( replace || _route === location.href ) {

                    history.replaceState(_state, document.title=_label, _route);

                } else {

                    history.pushState(state, document.title=_label, _route);

                }

            } finally {

                if ( modified ) { update; }

            }

        }

    }];

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function compile(table: Table): Switch {

    function pattern(glob: string): string { // convert a glob pattern to a regular expression
        return glob === "*" ? "^.*$" : `^${glob

            .replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&") // escape special regex characters
            .replace(/\\{(\w+)\\}/g, "(?<$1>[^/]+)") // convert glob named parameters
            .replace(/\\{\\}/g, "(?:[^/]+)") // convert glob anonymous parameters
            .replace(/\/\\\*$/, "(?<$>/.*)") // convert glob trailing path

        }([?#].*)?$`; // ignore trailing query/hash
    }

    const patterns: { [pattern: string]: string | Entry }={};

    for (const glob of Object.keys(table)) {
        patterns[pattern(glob)]=table[glob];
    }

    return route => {

        const entries: [string, string | Entry][]=Object
            .entries(patterns);

        const matches: [RegExpExecArray | null, string | Entry][]=entries
            .map(([pattern, component]) => [new RegExp(pattern).exec(route), component]);

        const [match, delegate]: [RegExpExecArray | null, string | Entry]=matches
            .find(([match]) => match !== null) || [null, () => null];

        if ( typeof delegate === "string" ) {

            return delegate.replace(/{(\w*)}|\/\*$/g, ($0, $1) => // replace wildcard references
                $0 === "/*" ? match?.groups?.$ || ""
                    : $1 ? match?.groups?.[$1] || ""
                        : route
            );

        } else {

            return createElement(
                (props, context) => delegate(props, context) || null, // guard against undefined results
                { ...match?.groups }
            );

        }

    };

}