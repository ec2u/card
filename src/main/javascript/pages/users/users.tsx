import React, { createElement, useEffect, useState } from "react";
import "./users.css";


interface Users {

    contains: User[];
}

interface User {

    admin: boolean;

    forename: string;
    surname: string;
    email: string;

}

export function CardUsers() {

    const [users, setUsers]=useState<Users>();

    useEffect(() => { // !!! factor useContext hook

        fetch("/users/", {

            headers: {
                Accept: "application/json"
            }

        })

            // !!! error control

            .then(response => response.json())
            .then(setUsers);

    }, []);

    return createElement("card-users", {}, <>{JSON.stringify(users)}!</>
        // <div>
        //     <div className={"topbar"}>
        //         <span>Users </span>
        //         <Link to="/users/add/" className="butons-add">
        //             <AiOutlinePlus/>
        //         </Link>
        //     </div>
        //
        //     <div className={"header"}>
        //         <div className="header-section">
        //             <span>forename</span>
        //             <span>surname</span>
        //             <span>email</span>
        //         </div>
        //         <div className="header-search">
        //             <Link to="/users/search" className="butons">
        //                 <FiSearch/>
        //             </Link>
        //         </div>
        //     </div>
        //     <div className={"divider"}></div>
        //     {response.map((data, i) => {
        //         return (
        //             <div className="data" key={i}>
        //                 <div className="data-section">
        //                     <span>{data.forename} </span>
        //                     <span>{data.surname}</span>
        //                     <span>{data.email}</span>
        //                 </div>
        //                 <div className="data-inspect">
        //                     <Link to="/users/inspect" className="butons-inspect">
        //                         <FaGreaterThan/>
        //                     </Link>
        //                 </div>
        //             </div>
        //         );
        //     })}
        // </div>
    );
}
