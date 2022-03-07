import { ChevronRight, Plus, Search } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./users.css";

interface Users {
  contains: User[];
}

interface User {
  admin: boolean;

  forename: string;
  surname: string;
  email: string;
  id: any;
}

export function CardUsers() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetch("/users/", {
      // !!! factor useContext hook
      headers: {
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => setUsers(data.contains));
  }, []);

  return createElement(
    "card-users",
    {},
    <div className={"users"}>
      <div className={"topnav"}>
        <span> Users</span>
        <span>
          <Link to="/users/add">
            <Plus size={38} className={"button-plus"} />
          </Link>
        </span>
      </div>

      <div className="grid-container">
        <table>
          <thead>
            <tr>
              <th>forename</th>
              <th>surname</th>
              <th>email</th>

              <th>
                <Search size={28} className={"button"} />
              </th>
            </tr>
          </thead>

          <hr className={"solid"} />

          <tbody>
            {users.map((data) => {
              return (
                <tr key={data.id}>
                  <td>{data.forename}</td>
                  <td>{data.surname}</td>
                  <td>{data.email}</td>

                  <td>
                    <Link to={`${data.id}`}>
                      <ChevronRight size={40} className={"button"} />
                    </Link>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

//   useEffect(() => {

//     fetch("/users/", {
//       method: "POST",
//       headers: {
//         Accept: "application/json",
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify(userdata),
//     })
//

//       .then((response) => response.json())
//       .then((data) => console.log(data));
//   }, []);

//   const userdata = {
//     label: "tony stark",
//     admin: false,
//     forename: "Tony",
//     surname: "Stark",
//     email: "Tony@argv.com",
//     id: "users/123",
//   };

//   fetch("/users/5679095853613056/", {
//     method: "PUT",
//     headers: {
//       Accept: "application/json",
//       "Content-Type": "application/json",
//     },
//     body: JSON.stringify(userdata),
//   })
//     // !!! error control

//     .then((response) => response.json())
//     .then((data) => console.log(data));
