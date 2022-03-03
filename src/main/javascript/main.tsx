import React, { useState, useEffect } from "react";
import axios from "axios";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Sidebar from "./components/sidebar";
import User from "./pages/users/user";
import Users from "./pages/users/users";
import Cards from "./pages/cards";
import Tokens from "./pages/tokens";
import "./index.css";
import Login from "./components/login";
import Inspect from "./pages/users/inspect";

const Main = () => {
  const [sidebar, setSidebar] = useState<boolean>(false);
  return (
    <div>
      <BrowserRouter>
        <Sidebar
          onCollapse={(sidebar: boolean) => {
            setSidebar(sidebar);
          }}
        />
        <div className={`container ${sidebar ? "inactive" : ""}`}>
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/user/" element={<User />} />
            <Route path="/users/" element={<Users />} />
             {/*<Route path="/users//" element={<Inspect />} /> */}
            <Route path="/cards/" element={<Cards />} />
            <Route path="/tokens/" element={<Tokens />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default Main;
