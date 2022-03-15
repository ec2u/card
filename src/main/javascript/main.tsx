/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

import React, { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./index.css";
import { Adduser } from "./pages/users/adduser";
import { Inspect } from "./pages/users/inspect";
import { CardUser } from "./pages/users/user";
import { CardUsers } from "./pages/users/users";
import Side from "./views/side";


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
            {/*<Route path="/" element={<Login/>}/>*/}
            <Route path="/user/" element={<CardUser />} />
            <Route path="/users/" element={<CardUsers />} />
            <Route path="/users/:id/" element={<Inspect />} />
            <Route path="/users/add" element={<Adduser />} />
            {/*<Route path="/cards/" element={<Cards/>}/>*/}
            {/*<Route path="/tokens/" element={<Tokens/>}/>*/}
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
}
