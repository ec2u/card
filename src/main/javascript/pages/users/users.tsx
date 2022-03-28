import { ChevronRight, Plus, Search } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import './users.css';



interface User {
  readonly admin: boolean;
  readonly forename: string;
  readonly surname: string;
  readonly email: string;
  readonly id: any;
}

export function CardUsers() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<Boolean>(false);


  useEffect(() => {

    const fetchData = async () => {
      setLoading(true)
      await fetch("/users/", {
        headers: {
          Accept: "application/json",
        },
      })
        .then((response) => response.json())
        .then((data) => setUsers(data.contains));
      setLoading(false)
    }
    fetchData();
  }, []);



  return createElement('card-users', {},
    <>


      <header>


        <a> Users </a>
        <a href='/users/add' title="add"> <Plus size={40} className={"button-plus"} /> </a>


      </header>


      <footer>

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

          {loading ?

            (<div className="spinner"></div>) : (
              <tbody>
                {users.map((user) => {

                  return (
                    <tr key={user.id} >

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

            )}
        </table>
      </footer>
    </>
  );

}

