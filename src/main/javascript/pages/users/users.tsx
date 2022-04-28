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
  const [search, setSearch] = useState<string>("");
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
    let number = clicked ? 1000 : 1
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchSubmit();
    }, number);
    setTimer(timerID)
  }, [search]);

  useEffect(() => {
    let number = clicked ? 1000 : 1
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchEmailSubmit();
    }, number);
    setTimer(timerID)
  }, [searchEmail]);


  const searchSubmit = () => {
    if (search === "") {
      fetchData("");
    } else {
      fetchData("filters?surnamePrefix=" + search);

    }
  }

  const searchEmailSubmit = () => {
    if (searchEmail === "") {
      fetchData("");
    } else {
      fetchData("filters?emailPrefix=" + searchEmail)
    }
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

      <table>
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
                  onChange={(e) => setSearch(e.target.value)}
                />
                <input
                  type="search"
                  className={"search-email"}
                  value={searchEmail}
                  onChange={(e) => setSearchEmail(e.target.value)} />
              </div>
              <div title="Close">
                <X size={28}
                  className={"close-button"}
                  onClick={() => {
                    setClicked(false);
                    setSearch("")
                    setSearchEmail("")
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

            <tbody>
              {users.map((user) => {

                return (
                  <tr key={user.id} >

                    <td>{user.forename}</td>
                    <td>{user.surname}</td>
                    <td>{user.email}</td>
                    <td>
                      <Link to={`${user.id}`} title={"inspect"}
                      >
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
