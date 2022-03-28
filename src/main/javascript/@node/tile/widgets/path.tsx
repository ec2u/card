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

import { Immutable, isArray } from "@metreeca/core";
import { Frame, isFrame, isValue, string, Value } from "@metreeca/core/value";
import * as React from "react";
import { createElement, ReactNode } from "react";
import "./path.css";


/**
 * Breadcrumbs path.
 */
export function NodePath({

	children

}: {

	children: Value | ReactNode | Immutable<Array<Value | ReactNode>>

}) {

	const steps=isArray(children) ? children as Array<string | Frame | ReactNode> : [children];

	return createElement("node-path", {}, steps.map((step, index) =>
			isFrame(step) && index+1 < steps.length ? <a key={step.id} href={step.id}>{string(step, navigator.languages)}</a>
				: isValue(step) ? <span key={string(step)}>{string(step)}</span>
					: <React.Fragment key={index}>{step}</React.Fragment>
		)
	);

}
