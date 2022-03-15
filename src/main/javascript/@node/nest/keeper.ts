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

import { isDefined, Optional } from "@metreeca/core";
import { createContext, createElement, ReactNode, useContext, useEffect, useState } from "react";


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

    /**
     * Updates the user profile.
     *
     * @param active initiates a login sequence if `true` or a `logout` sequence if `false`
     */
    (active: boolean): void;

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

    actions: { test, login, logout },

    children

}: {

    actions: {
        test: () => Promise<Optional<P>>,
        login: () => Promise<Optional<P>>,
        logout: () => Promise<Optional<P>>
    }

    children: ReactNode

}) {

    const [profile, setProfile]=useState<P>();


    useEffect(() => {

        if ( !isDefined(profile) ) {test().then(setProfile); }

    }, [profile]);


    return createElement(Context.Provider, {

        value: [

            profile as unknown,

            (active: boolean) => (active ? login : logout)().then(setProfile)

        ],

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
export function useKeeper<P>(): [Value<P>, Updater<P>] {
    return useContext<[Value<P>, Updater<P>]>(Context as any);
}
