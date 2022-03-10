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

  return (
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
            {users.map((user) => {
              return (
                <tr key={user.id}>
                  <td>{user.forename}</td>
                  <td>{user.surname}</td>
                  <td>{user.email}</td>

                  <td>
                    <Link to={`${user.id}`}>
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

