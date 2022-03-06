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
 * The app name as read from the `<title>` HTM head tag.
 */
export const name=document.title;

/**
 * The URL of the app icon as read from the `<link rel="icon">` HTML head tag.
 */
export const icon=(document.querySelector("link[rel=icon]") as HTMLLinkElement)?.href || "";


/**
 * The copyright of the app as read from the `<meta name="copyright">` HTML head tag.
 */
export const copy=(document.querySelector("meta[name=copyright]") as HTMLMetaElement)?.content || "";


/**
 * Creates a conditional `className` attribute spread.
 */
export function classes(classes: { [name: string]: boolean }): { className?: string } {
    return {

        className: Object.entries(classes)
            .filter(entry => entry[1])
            .map(entry => entry[0])
            .join(" ") || undefined

    };
}