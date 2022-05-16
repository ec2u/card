import { Contact, Key, User, Users } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import logo from "../index.svg";
import "./sidebar.css";



export const CardSidebar = (props: any) => {


  const [sidebar, setSidebar] = useState<boolean>(false);

  useEffect(() => {

    props.onCollapse(sidebar);

  }, [sidebar]);

  return (
    <div className={`side-menu ${sidebar ? "inactive" : ""}`}>
      <section>
        <div className="logo">
          <img
            src={logo}
            alt="EC2U logo"
            onClick={() => setSidebar(!sidebar)}
          />
          <span> EC2U Card</span>
        </div>
        <div className="items">
          <Link to="/cards/" className="items-logo" title="Cards">
            <Contact size={60} />
            <span> Cards</span>
          </Link>
        </div>
        <div className="items">
          <Link to="/users/" className="items-logo" title="Users">
            <Users size={60} />
            <span> Users </span>
          </Link>
        </div>
        <div className="items">
          <Link to="/tokens/" className="items-logo" title="Tokens">
            <Key size={60} />
            <span> Tokens  </span>
          </Link>
        </div>
      </section>
      <section>
        <div className=" items-name">
          <Link to="/user/" className="items-logo">
            <User size={60} />
            <span>Akhil</span>
          </Link>
        </div>
      </section>
    </div >
  );
};


