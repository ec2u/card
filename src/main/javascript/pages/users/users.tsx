import { ChevronRight, Plus, Search, X } from "lucide-react";
import React, { createElement, useCallback, useEffect, useRef, useState } from "react";
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
  const [error, setError] = useState<any>(null);
  const [search, setSearch] = useState<any>();
  const [searchEmail, setSearchEmail] = useState<string>("")
  const [clicked, setClicked] = useState<Boolean>(false);
  const [timer, setTimer] = useState<number>(0);


  const fetchData = async (searchData: any) => {
    setLoading(true)

    await fetch(`/users/` + searchData, {
      headers: {
        Accept: "application/json",
      }
    })
      .then((response) => response.json())
      .then((data) => setUsers(data.contains))
      .catch((error) => console.error(error))
    setLoading(false)
  }


  useEffect(() => {
    fetchData("")
  }, []);



  const searchSubmit = (e: string) => {
    if (e === "") {
      fetchData("");
    } else {
      fetchData("?label=" + e);

    }
  }


  const handleSearchLabelChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value)
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchSubmit(e.target.value)
    }, 1000);
    setTimer(timerID)
  }

  const searchEmailSubmit = (e: string) => {
    if (e === "") {
      fetchData("");
    } else {
      fetchData("?email=" + e)
    }
  }

  const handleSearchEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchEmail(e.target.value)
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchEmailSubmit(e.target.value)

    }, 1000);
    setTimer(timerID)
  }



  let SearchIcon =
    <div title={"search"}>
      <Search size={28}
        onClick={() => setClicked(true)}
        className={"search-button"}
      />
    </div>



  return createElement('card-users', {},

    <>

      <header>

        <a> Users </a>
        <a href='/users/add' title="nwe user">
          <Plus size={40} className={"button-plus"} />
        </a>

      </header>

      <table onBlur={() => setClicked(false)}>
        <thead>
          <tr>

            <th>forename</th>
            <th>surname</th>
            <th>email</th>
            <th>
              {SearchIcon}
            </th>

          </tr>
        </thead>

        <caption > <hr /> </caption>

        <caption>

          {/* when search fields are active... */}

          {clicked ? (

            <div className={"search-fields"}>
              <div className={"search-fields-start"}>
                <input
                  value={search}
                  type="search"
                  className={"search-label"}
                  onChange={handleSearchLabelChange}
                />
                <input
                  type="search"
                  className={"search-email"}
                  value={searchEmail}
                  onChange={handleSearchEmailChange} />
              </div>
              <div title="Close">
                <X size={28}
                  className={"close-button"}
                  onMouseDown={() => {
                    setClicked(false);
                    setSearch("");
                    setSearchEmail("");
                    fetchData("")
                  }}
                />
              </div>
            </div>



          ) : ("")}

        </caption>

        {loading ?

          (
            <>
              <caption className="spinner"></caption>

            </>


          ) : (

            <tbody >
              {users.map((user) => {


                return (

                  <tr key={user.id} >

                    <td>{user.forename}</td>
                    <td>{user.surname}</td>
                    <td>{user.email}</td>
                    <td>
                      <Link to={`${user.id}`} title={"inspect"}>

                        <ChevronRight size={40} className={"button-arrow"} />

                      </Link>
                    </td>


                  </tr>
                );
              })}
            </tbody>
          )}

      </table>

    </>
  );

}
