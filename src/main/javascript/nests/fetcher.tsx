/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import { NodeFetcher } from "@metreeca/nest/fetcher";
import React, { ReactNode } from "react";


export function CardFetcher({

    children

}: {

    children: ReactNode

}) {

    return <NodeFetcher interceptor={(fetcher, input, init={}) => {

        const headers=new Headers(init.headers || {});

        if ( !headers.get("Content-Type") ) {
            headers.set("Content-Type", "application/json");
        }

        if ( !headers.get("Cache-Control") ) {
            headers.set("Cache-Control", "no-cache, max-age=0, must-revalidate");
        }

        return fetcher(input, { ...init, headers });

    }}>{

        children

    }</NodeFetcher>;

}