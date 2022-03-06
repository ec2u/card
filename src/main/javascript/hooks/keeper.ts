import { Optional } from "@ec2u/card/hooks/index";
import { createContext, createElement, ReactNode, useContext, useState } from "react";


/**
 * Keeper context.
 *
 * Provides nested components with a {@link useKeeper| hook}-based shared state containing:
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
     * @param profile the updated user profile; `undefined` if logging out
     */
    (profile: Optional<P>): void;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

/**
 * Creates a keeper context.
 *
 * @param children the children components to be nested inside the new context component
 *
 * @return a new keeper context component
 *
 */
export function NodeKeeper<P>({

    children

}: {

    children: ReactNode

}) {

    const [profile, setProfile]=useState<P>();


    return createElement(Context.Provider, {

        value: [profile as unknown, (profile: Optional<unknown>) => setProfile(profile as Optional<P>)],

        children

    });

}
