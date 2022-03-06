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
 * Common TypeScript type definitions and object handling utilities.
 *
 * @module
 */

/**
 * Primitive types.
 */
export type Primitive=undefined | null | boolean | string | number


/**
 * Optional type.
 *
 * Denotes a possibly undefined value of the base type `T`
 *
 * @template T the base type
 */
export type Optional<T>=undefined | T


/**
 * Immutable type variant.
 *
 * Denotes an immutable value of the base type `T`
 *
 * @template T the base mutable type
 */
export type Immutable<T>=
    T extends Primitive | Function ? T
        : T extends Array<infer U> ? ReadonlyArray<Immutable<U>>
            : { readonly [K in keyof T]: Immutable<T[K]> }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Checks if a value is not `undefined`.
 */
export function isDefined<T>(value: undefined | T): value is T {
    return value !== undefined;
}

/**
 * Checks if a value is a boolean.
 */
export function isBoolean(value: unknown): value is boolean {
    return typeof value === "boolean";
}

/**
 * Checks if a value is a number.
 */
export function isNumber(value: unknown): value is number {
    return typeof value === "number";
}

/**
 * Checks if a value is a string.
 */
export function isString(value: unknown): value is string {
    return typeof value === "string";
}

/**
 * Checks if a value is an [xsd:date/time](https://www.w3.org/TR/xmlschema-2/#built-in-primitive-datatypes) string.
 *
 * @param value the value to be checked
 *
 * @return `true` if `value` is a string containing a valid lexical representation of an `xsd:date/time` value;
 * `false` otherwise
 */
export function isTemporal(value: unknown): value is string {
    return isDateTime(value) || isDate(value) || isTime(value);
}

/**
 * Checks if a value is an [xsd:dateTime](https://www.w3.org/TR/xmlschema-2/#dateTime) string.
 *
 * @param value the value to be checked
 *
 * @return `true` if `value` is a string containing a valid lexical representation of an `xsd:dateTime` value;
 * `false` otherwise
 */
export function isDateTime(value: unknown): value is string {
    return isString(value) && value.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(:\d{2}(\.\d+)?)?Z?$/) !== null;
}

/**
 * Checks if a value is an [xsd:date](https://www.w3.org/TR/xmlschema-2/#date) string.
 *
 * @param value the value to be checked
 *
 * @return `true` if `value` is a string containing a valid lexical representation of an `xsd:date` value;
 * `false` otherwise
 */
export function isDate(value: unknown): value is string {
    return isString(value) && value.match(/^\d{4}-\d{2}-\d{2}$/) !== null;
}

/**
 * Checks if a value is an [xsd:time](https://www.w3.org/TR/xmlschema-2/#time) string.
 *
 * @param value the value to be checked
 *
 * @return `true` if `value` is a string containing a valid lexical representation of an `xsd:time` value;
 * `false` otherwise
 */
export function isTime(value: unknown): value is string {
    return isString(value) && value.match(/^\d{2}:\d{2}(:\d{2}(\.\d+)?)?$/) !== null;
}

/**
 * Checks if a value is a plain object.
 *
 * @see https://stackoverflow.com/a/52694022/739773
 */
export function isObject(value: unknown): value is Record<any, any> & ({ bind?: never } | { call?: never }) {
    return value !== undefined && value !== null && Object.getPrototypeOf(value) === Object.prototype;
}

/**
 * Checks if a value is an array.
 */
export function isArray(value: unknown): value is unknown[] {
    return Array.isArray(value);
}


/**
 * Checks if a value is an empoty plain object or an empty array.
 */
export function isEmpty(value: unknown): value is ({} | []) {
    return isArray(value) ? value.length === 0
        : isObject(value) ? Object.keys(value).length === 0
            : false;
}

/**
 * Checks if a value is a function.
 */
export function isFunction(value: unknown): value is Function {
    return value instanceof Function;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Checks deep object equality.
 *
 * Object pairs are deeply equal if they contain:
 *
 * - two equal {@link Primitive primitive} values or two equal functions
 * - two {@link isObject plain objects} with deeply equal entry sets
 * - two {@link isArray arrays} with pairwise deeply equal items
 *
 * @param x the target object to be checked for equality
 * @param y the reference object to be checked for equality
 *
 * @return `true` if `x` and `y` are deeply equal; `false` otherwise
 */
export function equals(x: unknown, y: unknown): boolean {

    function objectEquals(x: { [s: string | number | symbol]: unknown }, y: typeof x) {
        return Object.entries(x).every(([label, value]) => equals(value, y[label]))
            && Object.entries(y).every(([label, value]) => equals(value, x[label]));
    }

    function arrayEquals(x: Array<unknown>, y: typeof x) {
        return x.length === 0 ? y.length === 0
            : x.length === y.length && x.every((value, index) => equals(value, y[index]));
    }

    return isObject(x) ? isObject(y) && objectEquals(x, y)
        : isArray(x) ? isArray(y) && arrayEquals(x, y)
            : Object.is(x, y);
}

/**
 * Makes an object immutable.
 *
 * @param value the value to be converted
 *
 * @return a deeply immutable clone of `value`
 */
export function immutable<T=any>(value: T): Immutable<typeof value> {
    if ( typeof value === "object" ) {

        return Object.freeze(Object.getOwnPropertyNames(value as any).reduce((object: any, key) => {

            object[key]=immutable((value as any)[key]);

            return object;

        }, Array.isArray(value) ? [] : {}));

    } else {

        return value as any;

    }
}
