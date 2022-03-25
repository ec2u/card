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


import { Immutable, isArray, isBoolean, isDefined, isNumber, isObject, isString } from "@metreeca/core/index";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export type Value=boolean | number | string | Local | State | Frame


export interface Local {

    readonly [lang: string]: string;

}

export interface State {

    readonly [field: string]: Value | Immutable<Value[]>;

}

export interface Frame extends Partial<State> {

    readonly id: string;
    readonly label?: string | Local;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function isValue(value: any): value is Value {
    return isBoolean(value) || isNumber(value) || isString(value)
        || isLocal(value) || isState(value) || isFrame(value);
}

export function isLocal(value: any): value is Local {
    return isObject(value) && !(value.id) && Object.entries(value).every(([key, value]) =>
        isString(key) && isString(value)
    );

}

export function isState(value: any): value is Local {
    return isObject(value) && Object.entries(value).every(([key, value]) =>
        isString(key) && (isValue(value) || isArray(value) && value.every(isValue))
    );

}

export function isFrame(value: any): value is Frame {
    return isString(value?.id)
        && (!isDefined(value?.label) || isString(value?.label) || isLocal(value?.label))
        && isState(value);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


export function string(value: Value, locales: string[] | readonly string []=[]): string {
    return isBoolean(value) ? value.toString()
        : isNumber(value) ? value.toLocaleString(locales as string[])
            : isString(value) ? value
                : isLocal(value) ? local(value, locales)
                    : isFrame(value) ? label(value, locales)
                        : "";
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function label(frame: Frame, locales: string[] | readonly string []) {

    console.log(frame.label);

    return frame.label && string(frame.label, locales) || guess(frame.id) || "";
}


function local(local: Local, locales: string[] | readonly string []) {
    return locales.map(l => local[l]).filter(s => s)[0] || local.en || Object.values(local)[0] || "";
}


/**
 * Guesses a resource label from its id.
 *
 * @param id the resource id
 *
 * @returns a label guessed from `id` or an empty string, if unable to guess
 */
function guess(id: string): string {
    return (id ?? "")
        .replace(/^.*?(?:[/#:]([^/#:{}]+))?(?:\/|#|#_|#id|#this)?$/, "$1") // extract label
        .replace(/([a-z-0-9])([A-Z])/g, "$1 $2") // split camel-case words
        .replace(/[-_]+/g, " ") // split kebab-case words
        .replace(/\b[a-z]/g, $0 => $0.toUpperCase()); // capitalize words
}
