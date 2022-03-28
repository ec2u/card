import { Check, X } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
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
    console.log('end of submit')
  };



  const handleChange = (e: any) => {
    const value = e.target.type === "checkbox" ? e.target.checked : e.target.value
    setAdduser((adduser) => ({
      ...adduser,
      [e.target.name]: value,
    }));
  };



  return (
    <div className="users">

      <div className="topnav-add">

        <span> Add User</span>

        <span>
          <a>
            {loading ? (<div className="spinner"></div>) : (<Check onClick={handleSubmit} size={38} className="button-check" />)}
          </a>


        </span>

        <span>
          <Link to='/users/'>
            < X size={40} className={'button-close'} />
          </Link>
        </span>

      </div>

      <div className="grid-container-add">


        <table>

          <thead>

            <tr className="tr-add">
              <th>forename</th>
              <th>surname</th>
              <th>email</th>

              <th className="th-admin"> admin</th>

            </tr>

          </thead>



          <tbody>

            <tr className="tr-add" >

              <td>

                <input
                  type="text"
                  required
                  name="forename"
                  value={adduser.forename}
                  onChange={handleChange}
                />

              </td>


              <td>

                <input
                  type="text"
                  required
                  name="surname"
                  value={adduser.surname}
                  onChange={handleChange}
                />

              </td>


              <td>

                <input
                  type="email"
                  required
                  name="email"
                  value={adduser.email}
                  onChange={handleChange}
                  className={'email'}
                />

              </td>


              <td>

                <input
                  className="checkbox"
                  type="checkbox"
                  name="admin"
                  onChange={handleChange}

                />

              </td>

            </tr>


          </tbody>

        </table>

      </div>

    </div>

  );
}
