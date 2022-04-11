import { ChevronRight, Plus, Search, X } from "lucide-react";
import React, { createElement, useCallback, useEffect, useRef, useState } from "react";
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
  const [clicked, setClicked] = useState<Boolean>(false);


  function useKey(key: number, cb: any) {

    const callbackRef = useRef(cb);
  }


  useEffect(() => {


    setLoading(true)
    const fetchData = async () => {
      await fetch("/users/", {
        headers: {
          Accept: "application/json",
        },
      })
        .then((response) => response.json())
        .then((data) => setUsers(data.contains))
        .catch((error) => console.warn(error))
      setLoading(false)
    }
    fetchData();
  }, []);


  const handleInput = (e: any) => {
    setSearch(e.target.value)

    if (e.keyCode === 27) {
      setClicked(false)
    }

  }

  const inputRef = useRef<HTMLInputElement>(null);

  let searchInput =
    <div className={"search-box"}
      onSubmit={(e) => inputRef.current?.blur()
      }
    >

      <input
        ref={inputRef}
        type="text"
        value={search}
        placeholder="search by surname"
        onChange={handleInput}


      />
      <X size={28}
        className={"close-button"}
        onClick={() => setClicked(false)}
      />

    </div>



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
              {clicked ? searchInput : SearchIcon}

            </th>

          </tr>

        </thead>

        <hr />

        {loading ?

          (<div className="spinner"></div>

          ) : (

            <tbody>
              {users.filter(user => user.surname.toLowerCase().includes(search.toLowerCase())).map((user) => {

                return (
                  <tr key={user.id} >

                    <td>{user.forename}</td>
                    <td>{user.surname}</td>
                    <td>{user.email}</td>
                    <td>
                      <a href={`${user.id}`} title={"inspect"}
                      >
                        <ChevronRight size={40} className={"button-arrow"} />
                      </a>
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


