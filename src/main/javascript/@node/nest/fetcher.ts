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

import { isNumber, isObject, isString, Optional } from "@metreeca/core";
import { Observable } from "@metreeca/core/observable";
import { createContext, createElement, ReactNode, useContext, useEffect, useReducer } from "react";


/**
 * Fetcher context.
 *
 * Provides nested components with a {@link useFetcher| head}-based shared state containing:
 *
 * - a shared fetch service
 * - a network activity status flag
 *
 * @module
 */

const Context=createContext<Optional<[Value, Updater]>>(undefined);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The value component of the fetcher context state.
 *
 * Holds `true`, if at least a fetch request is awaiting response; `false`, otherwise
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

export interface Interceptor {

    (fetcher: typeof fetch, input: RequestInfo, init?: RequestInit): Promise<Response>;

}


/**
 * Fetch error report.
 *
 */
export interface Report<D=any> {

    readonly status: number;
    readonly reason: string;
    readonly detail?: D;

}

export function isReport(value: unknown): value is Report {
    return isObject(value) && isNumber(value.status) && isString(value.reason);
}


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
 * Creates a fetcher context.
 *
 * @param fetcher the fetch function to be exposed by the new context; defaults to the global {@link fetch} function
 * @param children the children components to be nested inside the new context component
 *
 * @return a new fetcher context component
 *
 */
export function NodeFetcher({

    interceptor,

    children

}: {

    interceptor: Interceptor,

    children: ReactNode

}) {

    const [fetching, fetcher]=useFetcher();

    return createElement(Context.Provider, {

        value: [

            fetching,

            (input, init) => interceptor(fetcher, input, init)

        ],

        children

    });

}

const promises=new Set<Promise<Response>>();
const observable=Observable();

/**
 * Creates a fetcher context hook.
 *
 * @return a state tuple including a current {@link Value| value} and an {@link Updater| updater} function.
 */
export function useFetcher(): [Value, Updater] {

    const state=useContext(Context);

    if ( state ) { return state; } else {

        const [, update]=useReducer(v => v+1, 0);

        useEffect(() => observable.observe(update));

        return [

            promises.size > 0,

            (input, init={}) => {

                const method=(init.method || "GET").toUpperCase();
                const origin=new URL(isString(input) ? input : input.url, location.href).origin;
                const headers=new Headers(init.headers || {});

                // angular-compatible XSRF protection header

                if ( !Safe.has(method.toUpperCase()) && origin === location.origin ) {

                    let xsrf=(document.cookie.match(/\bXSRF-TOKEN\s*=\s*"?([^\s,;\\"]*)"?/) || [])[1];

                    if ( xsrf ) { headers.set("X-XSRF-TOKEN", xsrf); }

                }

                // error to synthetic response conversion

                const promise=fetch(input, { ...init, headers }).catch(reason =>
                    new Response(null, reason.name === "AbortError"
                        ? { status: FetchAborted, statusText: "Network Request Aborted" }
                        : { status: FetchFailed, statusText: "Network Request Failed" }
                    )
                );

                promises.add(promise);
                observable.notify();

                return promise.finally(() => {

                    promises.delete(promise);
                    observable.notify();

                });

            }

        ];

    }

}

