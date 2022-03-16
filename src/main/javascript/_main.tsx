/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import Side from "@ec2u/card/views/side";
import React, { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./index.css";
import { Addcard } from "./pages/cards/addcard";
import { CardInspect } from "./pages/cards/cardinspect";
import { VirtualCards } from "./pages/cards/cards";
import { Editcard } from "./pages/cards/editcard";
import { Adduser } from "./pages/users/adduser";
import { Edituser } from "./pages/users/edituser";
import { Inspect } from "./pages/users/inspect";
import { CardUser } from "./pages/users/user";
import { CardUsers } from "./pages/users/users";


export function CardMain() {

    const [sidebar, setSidebar]=useState<boolean>(false);

    return (
        <div>
            <BrowserRouter>
                <Side
                    onCollapse={(sidebar: boolean) => {
                        setSidebar(sidebar);
                    }}
                />

                <div className={`container ${sidebar ? "inactive" : ""}`}>
                    <Routes>

                        <Route path="/user/" element={<CardUser/>}/>
                        <Route path="/users/" element={<CardUsers/>}/>
                        <Route path="/users/:id/" element={<Inspect/>}/>
                        <Route path="/edit/users/:id/" element={<Edituser/>}/>
                        <Route path="/users/add" element={<Adduser/>}/>


                        <Route path="/cards/" element={<VirtualCards/>}/>
                        <Route path="/cards/:id/" element={<CardInspect/>}/>
                        <Route path="/edit/cards/:id/" element={<Editcard/>}/>
                        <Route path="/cards/add" element={<Addcard/>}/>


                        {/*<Route path="/tokens/" element={<Tokens/>}/>*/}
                    </Routes>
                </div>
            </BrowserRouter>
        </div>
    );
}
