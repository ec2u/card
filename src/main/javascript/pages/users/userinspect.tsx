import { Edit } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
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


  return createElement("card-inspectuser", {},
    <>
      <header>

        <section>
          <a href='/users/'>Users &#62;</a>
          <a>{user.label}</a>
        </section>

        <a href={`/edit${user.id}`}>
          <Edit size={38} className="button-edit" />
        </a>



      </header>

      <form>

        <div className={"start"}>

          <section>
            <label>forename</label>
            <span>{user.forename}</span>
          </section>

          <section>
            <label>surname</label>
            <span>{user.surname} </span>
          </section>

          <section>
            <label> email</label>
            <span>{user.email}</span>
          </section>
        </div>

        <div className={"end"}>
          <section>
            <label>admin</label>
            <input className="checkbox"
              type="checkbox"
              checked={user.admin}
              disabled />
          </section>
        </div>

      </form>

    </>
  );
}
