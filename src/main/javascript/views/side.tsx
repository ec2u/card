/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

import { icon } from "@metreeca/tile";
import React, { useEffect, useState } from "react";
import { AiOutlineIdcard, AiOutlineKey, AiOutlineUser } from "react-icons/ai";
import { FiUsers } from "react-icons/fi";
import { Link } from "react-router-dom";
import "../style.css";


const Side=(props: any) => {
    const [sidebar, setSidebar]=useState<boolean>(false);

    useEffect(() => {
        props.onCollapse(sidebar);
    }, [sidebar]);

    return (
        <div className={`side-menu ${sidebar ? "inactive" : ""}`}>
            <div className={"top-section"}>
                <div className={"logo"}>
                    <img
                        src={icon}
                        alt="EC2U logo"
                        onClick={() => setSidebar(!sidebar)}
                    />
                    <span> EC2U Card</span>
                </div>
                <div className={"items"}>
                    <Link to="/cards/" className="items-logo">
                        <AiOutlineIdcard/>
                        <span> Cards</span>
                    </Link>
                </div>
                <div className={"items"}>
                    <Link to="/users/" className="items-logo">
                        <FiUsers/>
                        <span> Users </span>
                    </Link>
                </div>
                <div className={"items"}>
                    <Link to="/tokens/" className="items-logo">
                        <AiOutlineKey/>
                        <span> Tokens </span>
                    </Link>
                </div>
                <div className={"items name"}>
                    <Link to="/user/" className="items-logo">
                        <AiOutlineUser/>
                        <span>Name</span>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default Side;
