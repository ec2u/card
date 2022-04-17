import { Edit } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./userinspect.css";

interface User {
  readonly admin: boolean;
  readonly forename: string;
  readonly surname: string;
  readonly email: string;
  readonly id: any;
  readonly label: string;
}



export function InspectUser() {
  const [user, setUser] = useState<User>({} as User);
  const [loading, setLoading] = useState<Boolean>(false)
  const { id } = useParams();


  useEffect(() => {
    setLoading(true)
    const fetchData = async () => {
      await fetch(`/users/${id}`, {
        headers: {
          Accept: "application/json",
        },
      })
        .then((response) => response.json())
        .then((data) => setUser(data));
      setLoading(false)
    }
    fetchData();
  }, []);


  return createElement("card-inspectuser", {},
    <>
      <header>

        <section>
          <a href='/users/' className={"users-link"}>Users &#8250;</a>
          <a>{user.label}</a>
        </section>

        <a href={`/edit${user.id}`}>
          <Edit size={38} className="button-edit" />
        </a>

      </header>

      {loading ? (
        <div className={"spinner"}></div>
      ) :
        (

          <form>

            <div className={"start"}>

              <section>
                <label>forename</label>
                <span>{user.forename}</span>
              </section >

              <section>
                <label>surname</label>
                <span>{user.surname} </span>
              </section>

              <section>
                <label> email</label>
                <span>{user.email}</span>
              </section>
            </div >

            <div className={"end"}>
              <section>
                <label>admin</label>
                <input className="checkbox"
                  type="checkbox"
                  checked={user.admin}
                  disabled />
              </section>
            </div>

          </form >

        )}



    </>
  );
}
