import { ChevronRight, Edit, Trash } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import "./inspect.css";

interface User {
  admin: boolean;

  forename: string;
  surname: string;
  email: string;
  id: any;
}

const user: User = {
  admin: false,

  forename: "",
  surname: "",
  email: "",
  id: "",
};

export function Inspect() {
  const [user, setUser] = useState<User>({} as User);
  const { id } = useParams();


  useEffect(() => {
    fetch(`/users/${id}`, {
      headers: {
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => setUser(data));
  }, []);


  return (
    <div className="users">
      <div className="topnav-inspect">
        <span> Users</span>
        <span>
          <ChevronRight size={35} />
        </span>

        <span>{user.surname}</span>
        <span>
          <Link to={`/edit${user.id}`}>
            <Edit size={38} className="button-plus" />
          </Link>
        </span>
      </div>

      <div className="grid-container-inspect">
        <table>
          <thead>
            <tr className="tr-inspect">
              <th>forename</th>
              <th>surname</th>
              <th>email</th>

              <th className="th-admin"> admin</th>
            </tr>
          </thead>

          <hr className="solid" />

          <tbody>
            <tr className="tr-inspect" key={user.id}>
              <td>{user.forename}</td>
              <td>{user.surname}</td>
              <td>{user.email}</td>
              <td >

                <input className="checkbox" type="checkbox" checked={user.admin} disabled />

              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}
