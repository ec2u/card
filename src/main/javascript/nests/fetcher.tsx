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

    return <NodeFetcher>{children}</NodeFetcher>;

}