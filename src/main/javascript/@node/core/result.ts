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

/**
 * Interfaces and utilities for managing async operations results.
 *
 * @module
 */

import { isFunction, isPresent, Optional } from "@metreeca/core";


/**
 * Async operation result.
 */
export interface Result<V, E=any> {

    <R>(probe: Probe<V, E, R>): R;

    <R>(probe: Partial<Probe<V, E, R>> & Fallback<V, E, R>): R;

    <R>(probe: Partial<Probe<V, E, R>>): Optional<R>;

}

/**
 * Result probe.
 *
 */
export interface Probe<V, E, R=void> {

    readonly fetch: R | ((abort: () => void) => R);

    readonly value: R | ((value: V) => R);

    readonly error: R | ((error: E) => R);

}

/**
 * Result probe fallback.
 */
export interface Fallback<V, E, R=void> {

    readonly other: R | ((state: (() => void) | V | E) => R);

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Creates a `fetch` {@link Result} object.
 *
 * @param fetch the callback for aborting the fetch operation
 *
 * @constructor
 */
export function Result<V, E>({ fetch }: {

    fetch: () => void

}): Result<V, E>;

/**
 * Creates a `value` {@link Result} object.
 *
 * @param value the value returned by the operation
 *
 * @constructor
 */
export function Result<V, E>({ value }: {

    value: V

}): Result<V, E>;


/**
 * Creates an `error` {@link Result} object.
 *
 * @param error the error reported by the operation
 *
 * @constructor
 */
export function Result<V, E>({ error }: {

    error: E,

}): Result<V, E>;

export function Result<V, E>({ fetch, value, error }: {

    fetch?: () => void,
    value?: V,
    error?: E,

}): Result<V, E> {

    return <R>(probe: any) => {

        if ( isPresent(fetch) ) {

            return isFunction(probe.fetch) ? probe.fetch(fetch) : isPresent(probe.fetch) ? probe.fetch
                : isFunction(probe.other) ? probe.other(fetch) : probe.other;

        } else if ( isPresent(value) ) {

            return isFunction(probe.value) ? probe.value(value) : isPresent(probe.value) ? probe.value
                : isFunction(probe.other) ? probe.other(value) : probe.other;

        } else if ( isPresent(error) ) {

            return isFunction(probe.error) ? probe.error(error) : isPresent(probe.error) ? probe.error
                : isFunction(probe.other) ? probe.other(error) : probe.other;

        } else {

            return undefined;

        }

    };

}
