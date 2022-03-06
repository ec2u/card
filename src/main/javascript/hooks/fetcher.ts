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

import { createContext, createElement, ReactNode, useContext, useState } from "react";


const Context=createContext<[Value, Updater]>([false, wrap(fetch)]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The value component of the fetcher context state.
 *
 * `true`, if at least a fetch request is awaiting response; `false`, otherwise
 */
export type Value=boolean;


/**
 * The updater component of the fetcher context state.
 *
 * Takes the same arguments as the global {@link fetch} function and returns a {@link Promise| promise} that always
 * resolves to a {@link Response| response}, converting error conditions into synthetic responses with special
 * internal error codes
 *
 * @see {@link FetchAborted}
 * @see {@link FetchFailed}
 */
export type Updater=typeof fetch;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Creates a {@link NodeFetcher| fetcher context} hook.
 *
 * @return a state tuple including a stateful {@link Value| value} and an {@link Updater| updater} function.
 */
export function useFetcher(): [Value, Updater] {
    return useContext(Context);
}

/**
 * Creates a fetcher context.
 *
 * Fetcher contexts are responsible for:
 *
 * - exposing a shared fetch service to nested {@link useFetcher| fetcher hooks}
 * - monitoring network activity status
 *
 * @param fetcher the fetch function to be handled by the new context; defaults to the global {@link fetch} function
 * @param children the children components to be nested inside the new context component
 *
 * @return a new fetcher context component
 *
 */
export function NodeFetcher({

    fetcher=fetch,

    children

}: {

    fetcher?: typeof fetch

    children: ReactNode

}) {

    const wrapped=wrap(fetcher);

    const [promises, setPromises]=useState(new Set<Promise<Response>>());


    function monitor(promise: Promise<Response>) {

        const update=new Set(promises);

        update.add(promise);

        setPromises(update);

        return promise.finally(() => {

            const update=new Set(promises);

            update.delete(promise);

            setPromises(update);

        });
    }


    return createElement(Context.Provider, {

        value: [

            promises.size > 0,

            (input, init) => monitor(wrapped(input, init))

        ],

        children

    });

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The internal status code used for reporting fetch aborts as synthetic responses.
 */
export const FetchAborted=499;

/**
 * The internal status code used for reporting fetch errors as synthetic responses.
 */
export const FetchFailed=599;


/**
 * The set of safe HTTP methods.
 *
 * @see {@link https://developer.mozilla.org/en-US/docs/Glossary/Safe/HTTP| MDN - Safe (HTTP Methods)}
 * @see {@link https://datatracker.ietf.org/doc/html/rfc7231#section-4.2.1| RFC 7231 - Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content - § 4.2.1. Safe Methods }
 */
export const Safe: Set<String>=new Set<String>(["GET", "HEAD", "OPTIONS", "TRACE"]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Resolves a URL.
 *
 * @param path the possibly relative URL to be resolved
 * @param base the base URL `path` is to be resolved against; defaults to {@link location.href}
 *
 * @returns a URL obtained by resolving `path` against `base`
 */
export function resolve(path: string, base: string=location.href): string {
    return new URL(path, base).href;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Wraps a fetcher with standard services.
 *
 * - injection of angular-compatible XSRF protection header
 * - conversion of network errors to synthetic HTTP status codes
 *
 * @param fetcher the fetcher function to be wrapped
 *
 * @return a wrapped version of `fetcher` supporting standard services
 */
function wrap(fetcher: typeof fetch): typeof fetch {
    return (input, init={}) => {

        const method=(init.method || "GET").toUpperCase();
        const origin=new URL(typeof input === "string" ? input : input.url, location.href).origin;
        const headers=new Headers(init.headers || {});

        // angular-compatible XSRF protection header

        if ( !Safe.has(method.toUpperCase()) && origin === location.origin ) {

            let xsrf=(document.cookie.match(/\bXSRF-TOKEN\s*=\s*"?([^\s,;\\"]*)"?/) || [])[1];

            if ( xsrf ) { headers.append("X-XSRF-TOKEN", xsrf); }

        }

        // error to synthetic response conversion

        return fetcher(input, { ...init, headers }).catch(reason =>
            new Response(null, reason.name === "AbortError"
                ? { status: FetchAborted, statusText: "Network Request Aborted" }
                : { status: FetchFailed, statusText: "Network Request Failed" }
            )
        );

    };
}