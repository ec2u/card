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

import { Optional } from "@metreeca/core";
import { Context, createContext, createElement, ReactNode, useContext } from "react";


/**
 * Keeper context.
 *
 * Provides nested components with a {@link useKeeper| head}-based shared state containing:
 *
 * - the current user profile
 *
 * @module
 */

const Context=createContext<[Value, Updater]>([undefined, () => {}]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The value component of the keeper context state.
 *
 * Holds a user profile of type `P`, if a user is authenticated; `undefined`, otherwise
 *
 *  @template P the type of the user profile
 */
export type Value<P=unknown>=Optional<P>;

/**
 * The updater component of the keeper context state.
 *
 *  @template P the type of the user profile
 */
export interface Updater<P=unknown> {

    (): void;

    (anonymous: null): void;

    (authenticated: P): void;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Creates a keeper context.
 *
 * @param children the children components to be nested inside the new context component
 *
 * @return a new keeper context component
 *
 */
export function NodeKeeper<P>({

    state,

    children

}: {

    state: [Value<P>, Updater<P>]

    children: ReactNode

}) {

    return createElement((Context as Context<[Value<P>, Updater<P>]>).Provider, {

        value: state,

        children

    });

}

/**
 * Creates a {@link NodeKeeper| keeper context} hook.
 *
 * @return a state tuple including a stateful {@link Value| value} and an {@link Updater| updater} function.
 *
 * @template P the type of the user profile
 */
export function useProfile<P>(): [Value<P>, Updater<P>] {
    return useContext<[Value<P>, Updater<P>]>(Context as any);
}

