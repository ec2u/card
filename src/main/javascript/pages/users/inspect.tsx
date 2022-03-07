import { Edit } from "lucide-react";
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
  const [user, setUser] = useState<User>();
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

  return createElement(
    "card-user",
    {},
    <div className={"users"}>
      <div className={"topnav"}>
        <span> Users</span>
        <span className={"button"}>
          <Link to={`/edit123`}>
            <Edit size={38} className={"button-plus"} />
          </Link>
        </span>
      </div>

      <div className="grid-container">
        <table>
          <thead>
            <tr className="tr-grid">
              <th>forename</th>
              <th>surname</th>
              <th>email</th>

              <th className="th-admin"> admin</th>
            </tr>
          </thead>

          <hr className={"solid"} />

          {/* <tbody>
            {user.map((data) => {
              return (
                <tr key={data.id}>
                  <td>{data.forename}</td>
                  <td>{data.surname}</td>
                  <td>{data.email}</td>
                </tr>
              );
            })}
          </tbody> */}
        </table>
      </div>
    </div>
  );
}
