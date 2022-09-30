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


export function AddUser() {
  const [adduser, setAdduser] = useState({
    forename: "",
    surname: "",
    email: "",
    admin: false
  } as Adduser);
  const [disable, setDisable] = useState<Boolean>(true);
  const [loading, setLoading] = useState<Boolean>(false);
  const [apiErrors, setapiErrors] = useState<string>("");
  const [click, setClick] = useState<Boolean>(false)
  const navigate = useNavigate();

  const userData = {
    admin: adduser.admin,
    forename: adduser.forename,
    surname: adduser.surname,
    email: adduser.email,
  };


  const validateEmail = (e: string) => {
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,5})+$/.test(e)) {
      return true;
    } else {
      return false
    }
  }


  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

    const value = e.target.type === "checkbox" ? e.target.checked : e.target.value


    if (adduser.forename === "" ||
      adduser.surname === "" ||
      value === "" ||
      ((e.target.name === "email") && !validateEmail(value.toString())) ||
      ((e.target.name != "email") && !validateEmail(adduser.email))

    ) {
      setDisable(true)
    }
    else {
      setDisable(false)
    }

    setAdduser((adduser) => ({
      ...adduser,
      [e.target.name]: value,
    }))
  }
  const handleAPiError = () => {
    setClick(!click)
  }


  const handleSubmit = async () => {

    if (disable) {

    } else {
      setLoading(true)
      await fetch("/users/", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      })
        .then((response) => {
          setLoading(false)
          if (response.ok) {
            navigate("/users/")
          } else {
            setapiErrors(response.status + "\n" + response.statusText);
            handleAPiError();

          }
        })

        .catch(error => console.error('error:', error));
    }

  };

  const errorMessage =
    <div className={"error-message"}>
      <div>
        Something Went Wrong ..!!! <span title="close" onClick={() => setClick(false)}> X </span>
      </div>
      <p>
        {apiErrors}
      </p>

    </div>



  return createElement("card-adduser", {},
    <>
      <header>
        <section>
          <a href="/users/" className={"users-link"} title="Users">Users &#8250;</a>
          <a>New user</a>
        </section>

        <section>
          <a title="save">
            {loading ? (<div className="spinner"></div>

            ) : (

              <Check onClick={handleSubmit} size={42}
                className="button-check"
                color={disable ? 'lightgray' : 'black'} />
            )}
          </a>
          <a href="/users/" title="close">
            <X size={42} className={'button-close'} />
          </a>
        </section>

      </header>
      <span>
        {click ? errorMessage : ""}
      </span>
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
              className={"forename"}
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
              className={"surname"}
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
