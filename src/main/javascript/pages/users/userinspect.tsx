import { ChevronRight, Edit, Trash } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import "./userinspect.css";

interface User {
  admin: boolean;

  forename: string;
  surname: string;
  email: string;
  id: any;
  label: string;
}



export function Inspect() {
  const [user, setUser] = useState<User>({} as User);
  const [loading, setLoading] = useState<Boolean>(false)
  const { id } = useParams();


  useEffect(() => {
    setLoading(true)
    fetch(`/users/${id}`, {
      headers: {
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => setUser(data));
    setLoading(false)
  }, []);


  return (
    <div className="users">
      <div className="topnav-inspect">

        <div className={"topnav-start"}>
          <div>
            <a href='/users/'>Users &gt;</a>
          </div>
          <div>
            <a>{user.label}</a>
          </div>

        </div>

        <div>
          <a>
            <Link to={`/edit${user.id}`}>
              <Edit size={38} className="button-plus" />
            </Link>
          </a>
        </div>
      </div>


      <form >
        {loading ? (<div className="spinner"></div>) : (
          <div className="data-inspect">

            <div className="data-start">

              <div className="data-section">
                <label>
                  forename
                </label>

                <span>
                  {user.forename}
                </span>

              </div>

              <div className="data-section">
                <label>
                  surname
                </label>

                <span>
                  {user.surname}
                </span>

              </div>

              <div className="data-section">
                <label>
                  email
                </label>

                <span>
                  {user.email}
                </span>

              </div>

            </div>
            <div className="data-end">
              <div className="end">
                <label>
                  admin
                </label>

                <input className="checkbox" type="checkbox" checked={user.admin} disabled />

              </div>
            </div>

          </div>
        )}

      </form>
    </div >
  );
}
