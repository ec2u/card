/*
 * Copyright © 2020-2025 EC2U Alliance
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

import { useStorage } from "@ec2u/card/hooks/storage";
import { createContext, createElement, ReactNode, useContext } from "react";


const Locale=(/^\w+\b/.exec(navigator.language))?.[0] || "en";

const Context=createContext<[string, (locale: string) => void]>([Locale, () => {}]);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function CardLocale({

    children

}: {

    children: ReactNode

}) {

    const [locale, setLocale]=useStorage<string>(localStorage, "locale", Locale);

    return createElement(Context.Provider, {

        value: [locale, setLocale],

        children

    });

}


export function useLocale(): [string, (locale: string) => void] {
    return useContext(Context);
}
