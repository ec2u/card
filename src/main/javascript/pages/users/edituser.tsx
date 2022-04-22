import { Check, Trash2, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
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



export function EditUser() {
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


  const userData = {
    admin: updateuser.admin,
    forename: updateuser.forename,
    surname: updateuser.surname,
    email: updateuser.email,
    id: updateuser.id,
    label: updateuser.label
  };

  const handleDelete = (id: number) => {

    fetch(`${id}`, {
      method: "DELETE",
    })
      .then(data => navigate('/users/'))
  };


  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.type === "checkbox" ? e.target.checked : e.target.value

    if (value === "" || updateuser.forename === "" ||
      updateuser.surname === "" || updateuser.email === ""
    ) {
      setDisable(true)
    }

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

    }
    else {
      setLoading(true)
      await fetch(`/users/${id}`, {
        method: "PUT",
        headers: {
          "Content-type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(userData),
      })
        .then(() => {
          setLoading(false)
          navigate(`${updateuser.id}`);
        })

        .catch(error => console.error("error:", error))
    }
  }


  let showCheck =

    < div title='Update' >
      {loading ? (<div className={"spinner"}></div>)
        : (
          <Check size={40} className={'check-button'}
            onClick={handleEdit}
            color={disable ? 'lightgray' : 'black'} />
        )}
    </div >

  let showTrash =
    <div title='Delete'>
      <Trash2 size={40} className={"trash-button"} onClick={(e) => Showpopup()} />
    </div>



  const handleonFocus = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.target.select();
    setClicked(true)
  }




  return createElement('card-edituser', {},
    <>

      <header>

        <section>
          <a href="/users/" className={"users-link"} title="Users">Users &#8250;</a>
          <a href={`${updateuser.id}`}>{updateuser.label}</a>
        </section>

        <section>
          <a>{clicked ? showCheck : showTrash}</a>
          <a href={`${updateuser.id}`} title='Close'>
            <X size={46} className={"close-button"} />
          </a>
        </section>


      </header>

      <form >

        <div className={'start'} >
          <section>
            <label>forename</label>
            <input
              required
              type='text'
              name="forename"
              className={"forename"}
              value={updateuser.forename}
              onChange={handleChange}
              onFocus={handleonFocus}
            />
          </section>

          <section>
            <label>surname</label>
            <input
              required
              type='text'
              name="surname"
              className={"surname"}
              value={updateuser.surname}
              onChange={handleChange}
              onFocus={handleonFocus}
            />
          </section>

          <section>
            <label>email</label>
            <input
              required
              type='email'
              className={"email"}
              name="email"
              value={updateuser.email}
              onChange={handleChange}
              onFocus={handleonFocus}
            />
          </section>

        </div>

        <div className={"end"}>
          <section>
            <label className="label-admin"> admin</label>
            <input
              className={"checkbox"}
              name='admin'
              type="checkbox"
              checked={updateuser.admin}
              onChange={handleChange}
              onFocus={handleonFocus}
            />
          </section>
        </div>


      </form>

      {
        dialog && (
          <Deletedialog
            handleyes={() => handleDelete(updateuser.id)}
            handleno={Showpopup}
          />
        )
      }

    </>
  );


}

