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

import { Optional } from "@metreeca/core";


/**
 * The absolute root URL with trailing slash.
 */
export const root: string=resolve("/");

/**
 * The absolute base URL with trailing slash.
 */
export const base: string=resolve((
    document.querySelector("base")?.href || import.meta.env.BASE_URL || "/"
).replace(/\/*$/, "/"), root);


/**
 * The app name as read from the `<title>` HTM head tag.
 */
export const name: string=document.title; // !!! remove

/**
 * The URL of the app icon as read from the `<link rel="icon">` HTML head tag.
 */
export const icon: string=(document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "";


/**
 * The app copyright as read from the `<meta name="copyright">` HTML head tag.
 */
export const copy=(document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || "";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Resolves a URL.
 *
 * @param path the possibly relative URL to be resolved
 * @param base the base URL `path` is to be resolved against; defaults to {@link location.href}
 *
 * @returns a URL obtained by resolving `path` against `base`
 */
export function resolve(path: string, base: string=location.href): string {
    return new URL(path, base).href;
}


/**
 * Creates a conditional `class` attribute.
 */
export function classes(classes: { [name: string]: Optional<boolean> }): Optional<string> {
    return Object.entries(classes)
        .filter(([, state]) => state)
        .map(([label]) => label)
        .join(" ") || undefined;
}