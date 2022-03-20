/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { immutable } from "@metreeca/core";


export interface Card {

    readonly code: string;
    readonly test: string;
    readonly expiry: string;

    readonly esi: string;
    readonly pic: number;
    readonly level: number;

    readonly name: string;
    readonly photo?: string;
    readonly email?: string;

}

export const CardLevels: Record<number, string>=immutable({ // !!! localize

    6: "Bachelor",
    7: "Master",
    8: "Doctorate"

});