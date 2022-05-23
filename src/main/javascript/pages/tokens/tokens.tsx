import { ChevronDown, ChevronRight, ChevronUp, Plus, Search, X, XCircle } from "lucide-react";
import React, { createElement, useCallback, useEffect, useRef, useState } from "react";
import './tokens.css';



interface Token {
    readonly label: string;
    readonly username: string;
    readonly password: string;
    readonly id: number | string;
    readonly tokenNumber: number;
}

export function CardTokens() {
    let searchUsername_data = sessionStorage.getItem('searchUsernameToken');
    let searchtokenNumber_data = sessionStorage.getItem('searchtokenNumberToken');
    const [tokens, setTokens] = useState<Token[]>([]);
    const [loading, setLoading] = useState<Boolean>(false);
    const [error, setError] = useState<any>(null);
    const [searchUsername, setSearchUsername] = useState<string>(searchUsername_data != null ? searchUsername_data : "");
    const [searchNumber, setsearchNumber] = useState<any>(searchtokenNumber_data != null ? searchtokenNumber_data : "");
    const [clicked, setClicked] = useState<Boolean>(false);
    const [timer, setTimer] = useState<number>(0);
    const [disable, setDisable] = useState<Boolean>(true);
    const [sorting, setSorting] = useState<string>("asc");
    const [sortingType, setSortingType] = useState<string>("")





    const fetchData = async (searchData: any) => {
        setLoading(true)

        await fetch("/tokens/" + searchData, {
            headers: {
                Accept: "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setTokens(data.contains))
            .catch((error) => setError(error))
        setLoading(false)
    }


    useEffect(() => {
        let search = sessionStorage.getItem('searching')
        if (search) {
            setClicked(true)
            switch (search) {
                case "username":
                    searchSubmit(searchUsername);
                    break;
                case "tokennumber":
                    hanldeSearchNumber(searchNumber);
                    break;
            }
            sessionStorage.removeItem('searching')
        } else
            fetchData("")
    }, []);



    const searchSubmit = (e: string) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?username=" + e);
            setDisable(false)
        }
    }

    const handleSearchUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchUsername(e.target.value)
        sessionStorage.setItem('searchUsernameToken', e.target.value);
        sessionStorage.setItem('searching', "username")
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const hanldeSearchNumber = (e: string) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?tokenNumber=" + e);
            setDisable(false)
        }
    }

    const searchNumberSubmit = (e: React.ChangeEvent<HTMLInputElement>) => {
        setsearchNumber(e.target.value)
        sessionStorage.setItem('searchtokenNumberToken', e.target.value);
        sessionStorage.setItem('searching', "tokennumber")
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            hanldeSearchNumber(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const usernameSorting = () => {
        setSearchUsername("")
        setSortingType("username")
        if (sorting === "asc") {
            let sort = "asc"
            fetchData("?sortingOrder=" + sort + "&sortingProperty=username")

            setSorting("desc")

        } else if (sorting === "desc") {
            let sort = "desc"
            setSorting("")
            fetchData("?sortingOrder=" + sort + " &sortingProperty=username")

        } else {
            setSorting("asc")
            fetchData("")
        }
    }

    const tokenNumberSorting = () => {
        setsearchNumber("")
        setSortingType("tokenNumber")
        if (sorting === "asc") {
            let sort = "asc"
            fetchData("?sortingOrder=" + sort + "&sortingProperty=tokenNumber")

            setSorting("desc")

        } else if (sorting === "desc") {
            let sort = "desc"
            setSorting("")
            fetchData("?sortingOrder=" + sort + "&sortingProperty=tokenNumber")

        } else {
            setSorting("asc")
            fetchData("")
        }
    }

    let SearchIcon =
        <div title={"search"}>
            <Search size={28}
                onClick={() => {
                    setClicked(!clicked)
                    setSearchUsername("");
                    setsearchNumber("")
                    sessionStorage.removeItem('searchUsernameToken');
                    sessionStorage.removeItem('searchtokenNumberToken');
                    fetchData("")
                }}
                className={"search-button"}
            />
        </div>

    const handleSearch = () => {
        if (disable) {

        } else {
            setClicked(false);
            setSearchUsername("");
            setsearchNumber("")
            sessionStorage.removeItem('searchUsernameToken');
            sessionStorage.removeItem('searchtokenNumberToken');
        }
    }

    return createElement('card-tokens', {},

        <>

            <header>

                <a> Tokens </a>
                <a href='/tokens/add' title="new token">
                    <Plus size={40} className={"button-plus"} />
                </a>

            </header>

            <table>

                <thead>

                    <tr>

                        <th onClick={usernameSorting}>username
                            {sortingType === "username" ? sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown /> : ""}
                        </th>
                        <th >password </th>
                        <th onClick={tokenNumberSorting}>token number
                            {sortingType === "tokenNumber" ? sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown /> : ""}
                        </th>
                        <th>
                            {SearchIcon}

                        </th>
                    </tr>
                </thead>

                <caption><hr /></caption>

                <caption>
                    {clicked ? (
                        <div className={"search-fields"}>
                            <div className={"search-fields-start"}>
                                <input
                                    type="search"
                                    className={"search-username"}
                                    value={searchUsername}
                                    onChange={handleSearchUsernameChange}
                                />
                                <input
                                    type="search"
                                    className={"search-token"}
                                    value={searchNumber}
                                    onChange={searchNumberSubmit}
                                />
                            </div>

                            <div title="Close">
                                <XCircle size={28}
                                    color={disable ? 'lightgray' : 'black'}
                                    className={"close-button"}
                                    onMouseDown={handleSearch}
                                />
                            </div>

                        </div>
                    ) : ("")}
                </caption>

                {loading ?

                    (<caption className="spinner"></caption>

                    ) : (

                        <tbody>
                            {tokens.map((token) => {

                                return (
                                    <tr key={token.id} >

                                        <td>{token.username}</td>
                                        <td>{token.password}</td>
                                        <td>{token.tokenNumber}</td>
                                        <td>
                                            <a href={`${token.id}`} title={"inspect"}
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


