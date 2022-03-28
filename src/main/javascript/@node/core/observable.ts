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


import { immutable } from "@metreeca/core/index";


export interface Observable {

	observe(observer: () => void): () => void;

	notify(): void;

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export function Observable(): Observable {

	const observers=new Set<() => void>();

	return immutable({

		observe(observer: () => void): () => void {

			observers.add(observer);

			return () => observers.delete(observer);

		},

		notify(): void {

			observers.forEach(observer => observer());

		}

	});

}
