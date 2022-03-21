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

import { Initial, Updater } from "@metreeca/hook/index";
import { useCallback, useEffect, useState } from "react";


export function useSessionStorage<T=any>(key: string, initial: Initial<T>): [T, Updater<T>] {
    return useStorage(sessionStorage, key, initial);
}

export function useLocalStorage<T=any>(key: string, initial: Initial<T>): [T, Updater<T>] {
    return useStorage(localStorage, key, initial);
}

export function useSharedStorage<T=any>(key: string, initial?: Initial<T>): [T, Updater<T>] {

    const [value, setValue]=useStorage(localStorage, key, initial);

    const sync=useCallback(() => {

        setValue(JSON.parse(localStorage.getItem(key) || "null"));

    }, [key]);

    useEffect(() => {

        window.addEventListener("storage", sync);

        return () => window.removeEventListener("storage", sync);

    }, [key]);

    return [value, setValue];
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function useStorage<T=any>(storage: Storage, key: string, initial: Initial<T>): [T, Updater<T>] {

    const [value, setValue]=useState<T>(() => {

        const item=storage.getItem(key);

        return item === null ? initial : JSON.parse(item);

    });

    return [value, value => {

        try { setValue(value); } finally {

            value === undefined
                ? storage.removeItem(key)
                : storage.setItem(key, JSON.stringify(value));

        }

    }];

}