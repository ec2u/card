import { Check, X } from "lucide-react";
import React, { createElement, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./adduser.css";


interface Adduser {
  admin: boolean;
  forename: string;
  surname: string;
  email: string;
}


export function Adduser() {
  const [adduser, setAdduser] = useState({} as Adduser);
  const [disable, setDisable] = useState<Boolean>(false);
  const [loading, setLoading] = useState<Boolean>(false);
  const navigate = useNavigate();

  const userdata = {
    admin: adduser.admin,
    forename: adduser.forename,
    surname: adduser.surname,
    email: adduser.email,
  };


  const handleSubmit = async () => {
    setLoading(true)
    await fetch("/users/", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userdata),
    })
      .then((response) => response.json())
      .catch(error => console.warn('error:', error));
    navigate('/users')

  };



  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.type === "checkbox" ? e.target.checked : e.target.value
    setAdduser((adduser) => ({
      ...adduser,
      [e.target.name]: value,
    }));
  };



  return createElement("card-adduser", {},
    <>
      <header>
        <section>
          <a>Add user</a>
        </section>

        <section>
          <a title="save">
            {loading ? (<div className="spinner"></div>

            ) : (

              <Check onClick={handleSubmit} size={42}
                className="button-check" />
            )}
          </a>
          <a href="/users/" title="close">
            <X size={42} className={'button-close'} />
          </a>
        </section>

      </header>

      <form>

        <div className={"start"}>

          <section>
            <label>forename</label>
            <input
              type="text"
              required
              name="forename"
              value={adduser.forename}
              onChange={handleChange}
            />
          </section>

          <section>
            <label>surname</label>
            <input
              type="text"
              required
              name="surname"
              value={adduser.surname}
              onChange={handleChange}
            />
          </section>

          <section>
            <label>email</label>
            <input
              type="email"
              required
              name="email"
              value={adduser.email}
              onChange={handleChange}
              className={'email'}
            />
          </section>

        </div>

        <div className={"end"}>

          <section>
            <label>admin</label>
            <input
              className="checkbox"
              type="checkbox"
              name="admin"
              onChange={handleChange}
            />
          </section>

        </div>
      </form>
    </>
  );
}
