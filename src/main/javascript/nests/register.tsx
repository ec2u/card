/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { useLocalStorage } from "@metreeca/hook/storage";
import React, { Context, createContext, createElement, ReactNode, useContext } from "react";


const Context=createContext<[Value<unknown>, Updater<unknown>]>([undefined, () => {}]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export type Value<C>=C;

export interface Updater<C> {

    (delta: Partial<C>): void;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardRegister<C>({

    state,

    children

}: {

    state: C

    children: ReactNode

}) {

    const [options, setOptions]=useLocalStorage<C>("node-register", state);


    return createElement((Context as Context<[Value<C>, Updater<C>]>).Provider, {

        value: [options as any, delta => setOptions({ ...options, ...delta })],

        children

    });

}


export function useOptions<C>(): [Value<C>, Updater<C>] {
    return useContext<[Value<C>, Updater<C>]>(Context as any);
}

