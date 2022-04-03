/*
 * Copyright © 2020-2022 EC2U Alliance
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

import { useState } from "react";


export function useStorage<T=any>(storage: Storage, key: string, initial: T | (() => T)): [T, (value: T) => void] {

    const [value, setValue]=useState<T>(() => {

        const item=storage.getItem(key);

        return item !== null ? JSON.parse(item)
            : initial instanceof Function ? initial()
                : initial;

    });

    return [value, value => {

        try { setValue(value); } finally {

            value === undefined
                ? storage.removeItem(key)
                : storage.setItem(key, JSON.stringify(value));

        }

    }];

}