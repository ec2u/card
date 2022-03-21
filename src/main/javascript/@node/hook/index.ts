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


export type Initial<V>=V | (() => V)

export type Setter<V>=React.Dispatch<V>
export type Updater<V>=React.Dispatch<React.SetStateAction<V>>

export type State<T>=[T, Setter<T>]
export type Slots<T>={ [K in keyof T]: State<T[K]> }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// export function slots<T>(state: State<Maybe<T>>): Maybe<Slots<T>>;
// export function slots<T, R>(state: State<Maybe<T>>, mapper: (slots: Slots<T>) => R): Maybe<R>;

export function slots<T, R>([value, setValue]: State<undefined | T>, mapper: ((slots: Slots<Exclude<typeof value, undefined>>) => R)): undefined | R {

	return value

		? mapper(Object.entries(value).reduce((slots, [k, v]) => ({

			...slots,

			[k]: [v, (u: typeof v) => setValue({ ...value, [k]: u })]

		}), {} as Slots<Exclude<typeof value, undefined>>))

		: undefined;

}
