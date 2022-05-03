import React, { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./index.css";

import { CardUser } from "./pages/user/user";
import { CardUsers } from "./pages/users/users";
import { CardSidebar } from "./views/sidebar";
import { InspectUser } from "./pages/users/userinspect";
import { AddUser } from "./pages/users/adduser";
import { EditUser } from "./pages/users/edituser";
import { VirtualCards } from "./pages/cards/cards";
import { CardInspect } from "./pages/cards/cardinspect";
import { EditCard } from "./pages/cards/editcard";
import { AddCard } from "./pages/cards/addcard";
import { CardTokens } from "./pages/tokens/tokens";
import { TokenInspect } from "./pages/tokens/inspecttoken";
import { EditToken } from "./pages/tokens/edittoken";
import { AddToken } from "./pages/tokens/addtoken";



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
            <Route path="/users:id" element={<InspectUser />} />
            <Route path="/edit/users:id/" element={<EditUser />} />
            <Route path="/users/add" element={<AddUser />} />



            <Route path="/cards/" element={<VirtualCards />} />
            <Route path="/cards:id/" element={<CardInspect />} />
            <Route path="/cards:id/edit" element={<EditCard />} />
            <Route path="/cards/add" element={<AddCard />} />



            <Route path="/tokens/" element={<CardTokens />} />
            <Route path="/tokens:id/" element={<TokenInspect />} />
            <Route path="/edit/tokens:id/" element={<EditToken />} />
            <Route path="/tokens/add" element={<AddToken />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
}
