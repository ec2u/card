import { ChevronDown, ChevronRight, ChevronUp, Plus, Search, X, XCircle } from "lucide-react";
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
  const [searchForename, setSearchForename] = useState<string>();
  const [searchSurname, setSearchSurname] = useState<string>();
  const [searchEmail, setSearchEmail] = useState<string>("")
  const [clicked, setClicked] = useState<Boolean>(false);
  const [timer, setTimer] = useState<number>(0);
  const [disable, setDisable] = useState<Boolean>(true)
  const [sorting, setSorting] = useState<string>("asc");
  const [adminSorting, setadminSorting] = useState<string>("true");
  const [sortingType, setSortingType] = useState<string>("");



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
      setDisable(true)
    } else {
      fetchData("?forename=" + e);
      setDisable(false)
    }
  }


  const handleSearchForenameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    setSearchForename(e.target.value)
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchSubmit(e.target.value)
    }, 1000);
    setTimer(timerID)
  }

  const searchSubmitSurname = (e: string) => {
    if (e === "") {
      fetchData("");
      setDisable(true)
    } else {
      fetchData("?surname=" + e);
      setDisable(false)
    }
  }

  const handleSearchSurnameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    setSearchSurname(e.target.value)
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchSubmitSurname(e.target.value)
    }, 1000);
    setTimer(timerID)
  }

  const searchEmailSubmit = (e: string) => {
    if (e === "") {
      fetchData("");
      setDisable(true)
    } else {
      fetchData("?email=" + e)
      setDisable(false)
    }
  }

  const handleSearchEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    setSearchEmail(e.target.value)
    clearTimeout(timer)
    let timerID = window.setTimeout(() => {
      searchEmailSubmit(e.target.value)

    }, 1000);
    setTimer(timerID)
  }

  const forenameSorting = () => {
    setSortingType("forename")
    if (sorting === "asc") {
      let sort = "asc"
      fetchData("?sortingOrder=" + sort + "&sortingProperty=forenameLowerCase")

      setSorting("desc")

    } else if (sorting === "desc") {
      let sort = "desc"
      setSorting("")
      fetchData("?sortingOrder=" + sort + "&sortingProperty=forenameLowerCase")

    } else {
      setSorting("asc")
      fetchData("")
    }

  }
  const surnameSorting = () => {
    setSortingType("surname")
    if (sorting === "asc") {
      let sort = "asc"
      fetchData("?sortingOrder=" + sort + "&sortingProperty=forenameLowerCase")

      setSorting("desc")

    } else if (sorting === "desc") {
      let sort = "desc"
      setSorting("")
      fetchData("?sortingOrder=" + sort + "&sortingProperty=forenameLowerCase")

    } else {
      setSorting("asc")
      fetchData("")
    }
  }


  const emailSorting = () => {
    setSortingType("email")
    if (sorting === "asc") {
      let sort = "asc"
      fetchData("?sortingOrder=" + sort + "&sortingProperty=forenameLowerCase")

      setSorting("desc")

    } else if (sorting === "desc") {
      let sort = "desc"
      setSorting("")
      fetchData("?sortingOrder=" + sort + "&sortingProperty=forenameLowerCase")

    } else {
      setSorting("asc")
      fetchData("")
    }
  }

  const handleAdminChange = () => {
    if (adminSorting === "true") {
      let sort = "true"
      fetchData("?isAdmin=" + sort)

      setadminSorting("false")

    } else if (adminSorting === "false") {
      let sort = "false"
      setadminSorting("")
      fetchData("?isAdmin=" + sort)

    } else {
      setadminSorting("true")
      fetchData("")
    }
  }




  const handleSearchClose = () => {
    if (disable) {

    } else {
      setClicked(false);
      setSearchForename("");
      setSearchSurname("");
      setSearchEmail("");
      fetchData("")
    }
  }


  let SearchIcon =
    <div title={"search"}>
      <Search size={28}
        onClick={() => setClicked(!clicked)}
        className={"search-button"}
      />
    </div>




  return createElement('card-users', {},

    <>

      <header>

        <a> Users </a>
        <a href='/users/add' title="new user">
          <Plus size={40} className={"button-plus"} />
        </a>

      </header>

      <table onBlur={() => setClicked(false)}>
        <thead>
          <tr>
            <th onClick={handleAdminChange} >admin</th>

            <th onClick={forenameSorting}>forename
              {sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown />}
            </th>
            <th onClick={surnameSorting}>surname
              {sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown />}
            </th>
            <th onClick={emailSorting}>email
              {sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown />}
            </th>
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
                  value={searchForename}
                  type="search"
                  className={"search-forename"}
                  onChange={handleSearchForenameChange}
                />
                <input
                  className={"search-surname"}
                  type="search"
                  value={searchSurname}
                  onChange={handleSearchSurnameChange}
                />
                <input
                  type="search"
                  className={"search-email"}
                  value={searchEmail}
                  onChange={handleSearchEmailChange} />
              </div>
              <div title="Close">
                <XCircle size={28}
                  className={"close-button"}
                  color={disable ? 'lightgray' : 'black'}
                  onMouseDown={handleSearchClose}
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
                    <td><input className="checkbox"
                      type="checkbox"
                      checked={user.admin}
                      disabled /> </td>
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
