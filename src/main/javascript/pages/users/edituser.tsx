import { Check, ChevronRight, Trash, X } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { Dialog } from "./dialog";
import "./edituser.css";

interface User {
  admin: boolean;
  label: string;
  forename: string;
  surname: string;
  email: string;
  id: any;
}

export function Edituser() {
  const [updateuser, setUpdateuser] = useState<User>({} as User);
  const [dialog, setDialog] = useState<Boolean>(false);
  const [checked, setChecked] = useState<Boolean>(true);
  const { id } = useParams();
  const navigate = useNavigate();




  useEffect(() => {
    fetch(`/users/${id}`, {
      headers: {
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => setUpdateuser(data));
  }, []);

  const handleChange = (event: any) => {
    setUpdateuser((updateuser) => ({
      ...updateuser,
      [event.target.name]: event.target.value,
    }));
  };

  const userdata = {
    admin: updateuser.admin,
    forename: updateuser.forename,
    surname: updateuser.surname,
    email: updateuser.email,
    id: updateuser.id,
    label: updateuser.label
  };

  const handleEdit = () => {
    fetch(`/users/${id}`, {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(userdata),
    }).then((response) => response.json());
  };

  const handleDelete = (id: any) => {
    fetch(`${id}`, {
      method: "DELETE",
    })
      .then(data => navigate('/users/'))
  };

  const Showpopup = () => {
    setDialog(!dialog);
  };

  return (
    <div>
      <div className={"users"}>
        <div className={"topnav-edit"}>
          <div> <Link className={'users-link'} to='/users/'>Users</Link></div>
          <div>
            <ChevronRight size={35} />
          </div>

          <div>{updateuser.surname}</div>
          <div>
            <Link to={`${updateuser.id}`}>
              < Check size={40} onClick={handleEdit} />
            </Link>

          </div>
          <div>
            <Trash size={38} className={"button-trash"} onClick={Showpopup} />
          </div>
          <div>
            <Link to={`${updateuser.id}`}>
              <X size={42} className={"button-trash"} />
            </Link>
          </div>
        </div>

        <div className="grid-container-edit">
          <table>
            <thead>
              <tr className="tr-edit">
                <th>forename</th>
                <th>surname</th>
                <th>email</th>
                <th>label</th>
                <th>id</th>

                <th className="th-admin"> admin</th>
              </tr>
            </thead>

            <hr className={"solid"} />

            <tbody>
              <tr className="tr-edit">
                <td>
                  <input
                    required
                    type='text'
                    name="forename"
                    value={updateuser.forename}
                    onChange={handleChange}
                  />
                </td>
                <td>
                  <input
                    required
                    type='text'
                    name="surname"
                    value={updateuser.surname}
                    onChange={handleChange}
                  />
                </td>
                <td>
                  <input
                    required
                    type='email'
                    className="email"
                    name="email"
                    value={updateuser.email}
                    onChange={handleChange}
                  />
                </td>
                <td>
                  <input
                    type='text'
                    disabled
                    className="label"
                    name="label"
                    value={updateuser.label}
                    onChange={handleChange}
                  />
                </td>
                <td>
                  <input
                    className="id"
                    disabled
                    name="id"
                    value={updateuser.id}
                    onChange={handleChange}
                  />
                </td>
                <td>
                  <input
                    required
                    type='checkbox'
                    className="admin"
                    name="admin"
                    value={updateuser.admin}
                    onChange={handleChange}

                  />

                </td>

              </tr>
            </tbody>
          </table>
        </div>
      </div>
      {dialog && (
        <Dialog
          handleyes={() => handleDelete(updateuser.id)}
          handleno={Showpopup}
        />
      )}
    </div>
  );
}
