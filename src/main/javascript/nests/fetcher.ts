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

import { wrap } from "@metreeca/nests/network";
import { createContext, createElement, ReactNode, useContext, useState } from "react";


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

const Context=createContext<[Value, Updater]>([false, wrap(fetch)]);


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

/**
 * Creates a fetcher context hook.
 *
 * @return a state tuple including a current {@link Value| value} and an {@link Updater| updater} function.
 */
export function useFetcher(): [Value, Updater] {
    return useContext(Context);
}
