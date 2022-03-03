import React, { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Sidebar from "./components/sidebar";
import "./index.css";
import { CardUser } from "./pages/users/user";
import { CardUsers } from "./pages/users/users";


export function CardMain() {

    const [sidebar, setSidebar]=useState<boolean>(false);

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
                        {/*<Route path="/" element={<Login/>}/>*/}
                        <Route path="/user/" element={<CardUser/>}/>
                        <Route path="/users/" element={<CardUsers/>}/>
                        {/*<Route path="/users//" element={<Inspect />} /> */}
                        {/*<Route path="/cards/" element={<Cards/>}/>*/}
                        {/*<Route path="/tokens/" element={<Tokens/>}/>*/}
                    </Routes>
                </div>

            </BrowserRouter>

        </div>
    );
}
