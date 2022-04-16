import React, { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./index.css";

import { CardUser } from "./pages/user/user";
import { CardUsers } from "./pages/users/users";
import { CardSidebar } from "./views/sidebar";
import { Inspect } from "./pages/users/userinspect";
import { Adduser } from "./pages/users/adduser";
import { Edituser } from "./pages/users/edituser";
import { VirtualCards } from "./pages/cards/cards";
import { CardInspect } from "./pages/cards/cardinspect";
import { Editcard } from "./pages/cards/editcard";
import { Addcard } from "./pages/cards/addcard";
import { CardTokens } from "./pages/tokens/tokens";
import { TokenInspect } from "./pages/tokens/inspecttoken";
import { Edittoken } from "./pages/tokens/edittoken";
import { Addtoken } from "./pages/tokens/addtoken";



export function CardRoutes() {
  const [sidebar, setSidebar] = useState<boolean>(false);

  return (
    <div>
      <BrowserRouter>
        <CardSidebar
          onCollapse={(sidebar: boolean) => {
            setSidebar(sidebar);
          }}
        />

        <div className={`container ${sidebar ? "inactive" : ""}`}>
          <Routes>

            <Route path="/user/" element={<CardUser />} />
            <Route path="/users/" element={<CardUsers />} />
            <Route path="/users/:id/" element={<Inspect />} />
            <Route path="/edit/users/:id/" element={<Edituser />} />
            <Route path="/users/add" element={<Adduser />} />



            <Route path="/cards/" element={<VirtualCards />} />
            <Route path="/cards/:id/" element={<CardInspect />} />
            <Route path="/edit/cards/:id/" element={<Editcard />} />
            <Route path="/cards/add" element={<Addcard />} />



            <Route path="/tokens/" element={<CardTokens />} />
            <Route path="/tokens/:id/" element={<TokenInspect />} />
            <Route path="/edit/tokens/:id/" element={<Edittoken />} />
            <Route path="/tokens/add" element={<Addtoken />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
}
