/*
 * Copyright © 2020-2021 Metreeca srl
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


import { isDefined, isFunction, Optional } from "@ec2u/card/hooks/index";


export interface Result<V, E=any> {

    <R>(probe: Probe<V, E, R>): R;

    <R>(probe: Partial<Probe<V, E, R>> & Fallback<V, E, R>): R;

    <R>(probe: Partial<Probe<V, E, R>>): Optional<R>;

}

export interface Probe<V, E, R=void> {

    readonly fetch: R | ((abort: () => void) => R);

    readonly value: R | ((value: V) => R);

    readonly error: R | ((error: E) => R);

}

export interface Fallback<V, E, R=void> {

    readonly other: R | ((state: (() => void) | V | E) => R);

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function Result<V, E>({ fetch }: {

    fetch: () => void

}): Result<V, E>;

export function Result<V, E>({ value }: {

    value: V

}): Result<V, E>;

export function Result<V, E>({ error }: {

    error: E,

}): Result<V, E>;

export function Result<V, E>({ fetch, value, error }: {

    fetch?: () => void,
    value?: V,
    error?: E,

}): Result<V, E> {

    return <R>(probe: any) => {

        if ( isDefined(fetch) ) {

            return isFunction(probe.fetch) ? probe.fetch(fetch)
                : isDefined(probe.fetch) ? probe.fetch
                    : isFunction(probe.other) ? probe.other(fetch)
                        : probe.other;

        } else if ( isDefined(value) ) {

            return isFunction(probe.value) ? probe.value(value)
                : isDefined(probe.value) ? probe.value
                    : isFunction(probe.other) ? probe.other(value)
                        : probe.other;

        } else if ( isDefined(error) ) {

            return isFunction(probe.error) ? probe.error(error)
                : isDefined(probe.error) ? probe.error
                    : isFunction(probe.other) ? probe.other(error)
                        : probe.other;

        } else {

            return undefined;

        }

    };

}
