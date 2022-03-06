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

import { equals, immutable } from "./index";


describe("equals()", () => {

    it("should handle primitive values", async () => {

        expect(equals(undefined, undefined)).toBeTruthy();
        expect(equals(undefined, null)).toBeFalsy();

        expect(equals(null, null)).toBeTruthy();
        expect(equals(null, true)).toBeFalsy();

        expect(equals(true, true)).toBeTruthy();
        expect(equals(true, false)).toBeFalsy();
        expect(equals(true, "true")).toBeFalsy();

        expect(equals("x", "x")).toBeTruthy();
        expect(equals("x", "y")).toBeFalsy();
        expect(equals("x", 0)).toBeFalsy();

        expect(equals(0, 0)).toBeTruthy();
        expect(equals(0, 1)).toBeFalsy();
        expect(equals(0, undefined)).toBeFalsy();

        expect(equals("x", {})).toBeFalsy();
        expect(equals("x", [])).toBeFalsy();

    });

    it("should handle functions", async () => {

        const uno=() => 1;
        const due=() => 2;

        expect(equals(uno, uno)).toBeTruthy();
        expect(equals(uno, due)).toBeFalsy();
        expect(equals(uno, {})).toBeFalsy();

    });

    describe("equals({})", () => {

        it("should handle empty objects", async () => {

            expect(equals({}, {})).toBeTruthy();
            expect(equals({}, { uno: 1 })).toBeFalsy();

        });

        it("should handle flat objects", async () => {

            expect(equals({ uno: 1, due: 2 }, { uno: 1, due: 2 })).toBeTruthy();
            expect(equals({ uno: 1, due: 2 }, { uno: 1 })).toBeFalsy();
            expect(equals({ uno: 1, due: 2 }, { uno: 1, tre: 3 })).toBeFalsy();
            expect(equals({ uno: 1, due: 2 }, { uno: 1, due: 2, tre: 3 })).toBeFalsy();

        });

        it("should ignore entry order", async () => {

            expect(equals({ uno: 1, due: 2, tre: 3 }, { due: 2, tre: 3, uno: 1 })).toBeTruthy();

        });

        it("should handle nested objects", async () => {

            expect(equals({ nested: { uno: 1, due: 2 } }, { nested: { uno: 1, due: 2 } })).toBeTruthy();
            expect(equals({ nested: { uno: 1, due: 2 } }, { nested: { uno: 1, tre: 3 } })).toBeFalsy();

        });

        it("should handle nested arrays", async () => {

            expect(equals({ nested: [1, 2] }, { nested: [1, 2] })).toBeTruthy();
            expect(equals({ nested: [1, 2] }, { nested: [1, 3] })).toBeFalsy();

        });

    });

    describe("equals([])", () => {

        it("should handle empty arrays", async () => {

            expect(equals([], [])).toBeTruthy();
            expect(equals([], [1])).toBeFalsy();

        });

        it("should handle flat arrays", async () => {

            expect(equals([1, 2], [1, 2])).toBeTruthy();
            expect(equals([1, 2], [1])).toBeFalsy();
            expect(equals([1, 2], [1, 3])).toBeFalsy();
            expect(equals([1, 2], [1, 2, 3])).toBeFalsy();

        });

        it("should consider item order", async () => {

            expect(equals([1, 2, 3], [1, 2, 3])).toBeTruthy();
            expect(equals([1, 2, 3], [2, 3, 1])).toBeFalsy();

        });

        it("should handle nested arrays", async () => {

            expect(equals([1, [2, 3]], [1, [2, 3]])).toBeTruthy();
            expect(equals([1, [2, 3]], [1, [3, 2]])).toBeFalsy();

        });

        it("should handle nested objects", async () => {

            expect(equals([{ uno: 1, due: 2 }], [{ uno: 1, due: 2 }])).toBeTruthy();
            expect(equals([{ uno: 1, due: 2 }], [{ uno: 1, tre: 3 }])).toBeFalsy();

        });

    });

});

describe("immutable()", () => {

    it("should return input primitives", async () => {

        const value="x";
        const clone=immutable(value);

        expect(clone).toBe(value);

    });

    it("should return an immutable object clone", async () => {

        const value={ uno: 1, due: 2 };
        const clone=immutable(value);

        expect(clone).not.toBe(value);
        expect(clone).toEqual(value);

        expect(() => (clone as any).uno=3).toThrow();

    });

    it("should return an immutable array clone", async () => {

        const value=[1, 2];
        const clone=immutable(value);

        expect(clone).not.toBe(value);
        expect(clone).toEqual(value);

        expect(() => (clone as any)[1]=3).toThrow();

    });

});