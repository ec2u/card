import { Check, ChevronRight, Trash2, X } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { Deletedialog } from "./deletedialog";
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
  const [updateuser, setUpdateuser] = useState<User>({
    admin: false,
    label: '',
    forename: '',
    surname: '',
    email: '',
    id: '',
  });
  const [dialog, setDialog] = useState<Boolean>(false);
  const [clicked, setClicked] = useState<Boolean>(false);
  const [disable, setDisable] = useState<Boolean>(false)
  const [loading, setLoading] = useState<Boolean>(false)

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


  const userdata = {
    admin: updateuser.admin,
    forename: updateuser.forename,
    surname: updateuser.surname,
    email: updateuser.email,
    id: updateuser.id,
    label: updateuser.label
  };

  const handleDelete = (id: any) => {

    fetch(`${id}`, {
      method: "DELETE",
    })
      .then(data => navigate('/users/'))
  };


  const handleChange = (e: any) => {
    const value = e.target.type === "checkbox" ? e.target.checked : e.target.value

    if (value === "") { setDisable(true) }

    else {
      setDisable(false)
    }

    setUpdateuser((updateuser) => ({
      ...updateuser,
      [e.target.name]: value,
    }))
  }

  const Showpopup = () => {
    setDialog(!dialog);
  };


  const handleEdit = async () => {

    if (disable) {

    } else {
      setLoading(true)
      await fetch(`/users/${id}`, {
        method: "PUT",
        headers: {
          "Content-type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(userdata),
      })
        .then((response) => response.json())
        .catch(error => console.warn("error:", error))
      setLoading(false)
      navigate(`${updateuser.id}`);

    }
  };




  let showCheck =

    < a title='Update' >
      {loading ? (<div className="spinner"></div>) : (
        <Check size={40} className={'check-button'} onClick={handleEdit} color={disable ? 'lightgray' : 'black'} />
      )}

    </a >





  let showTrash =
    <a title='Delete'>
      <Trash2 size={40} className={"trash-button"} onClick={(e) => Showpopup()} />
    </a>



  const handleonFocus = (e: any) => {
    setClicked(true)
  }


  // const handleonBlur = (e: any) => {
  //   setClicked(false)
  // }


  return (
    <div className="main">
      <div className="users">
        <div className={"topnav-edit"}>
          <div className="topnav-start">

            <div>
              <a href="/users/"> Users &gt;</a>
            </div>
            <div>
              <a href={`${updateuser.id}`}>{updateuser.label}</a>
            </div>

          </div>

          <div >
            <div className="navend">
              <label>
                {clicked ? showCheck : showTrash}
              </label>



              <a href={`${updateuser.id}`} title='Close'>
                <X size={46} className={"close-button"} />
              </a>
            </div>

          </div>
        </div>


        <form >


          <div className="data-edit">
            <div className="data-start">

              <div className="data-section">
                <label>forename</label>

                <input
                  required
                  type='text'
                  name="forename"
                  className="forename"
                  value={updateuser.forename}
                  onChange={handleChange}
                  onFocus={handleonFocus}
                // onBlur={handleonBlur}
                />
              </div>


              <div className="data-section">
                <label>surname</label>

                <input
                  required
                  type='text'
                  name="surname"
                  className="surname"
                  value={updateuser.surname}
                  onChange={handleChange}
                  onFocus={handleonFocus}
                // onBlur={handleonBlur}
                />
              </div>

              <div className="data-section">
                <label>email</label>

                <input
                  required
                  type='email'
                  className="email"
                  name="email"
                  value={updateuser.email}
                  onChange={handleChange}
                  onFocus={handleonFocus}
                // onBlur={handleonBlur}
                />
              </div>

            </div>

            <div className="data-end">
              <div className="end">
                <label className="label-admin"> admin</label>

                <input className="checkbox" name='admin' type="checkbox" checked={updateuser.admin} onChange={handleChange} onFocus={handleonFocus}
                // onBlur={handleonBlur}
                />
              </div>

            </div>

          </div>


        </form>
      </div>

      {
        dialog && (
          <Deletedialog
            handleyes={() => handleDelete(updateuser.id)}
            handleno={Showpopup}
          />
        )
      }
    </div >
  );
}

