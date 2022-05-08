import { ChevronRight, Plus, Search, X } from "lucide-react";
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
    const [tokens, setTokens] = useState<Token[]>([]);
    const [loading, setLoading] = useState<Boolean>(false);
    const [error, setError] = useState<any>(null);
    const [search, setSearch] = useState<string>("");
    const [searchNumber, setsearchNumber] = useState<any>();
    const [clicked, setClicked] = useState<Boolean>(false);
    const [timer, setTimer] = useState<number>(0);
    const [disable, setDisable] = useState<Boolean>(true);
    const [sorting, setSorting] = useState<string>("asc")



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
        setSearch(e.target.value)
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
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            hanldeSearchNumber(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const usernameSorting = () => {

        if (sorting === "desc") {
            setSorting("asc")
        }
        else {
            setSorting("desc")
        }

        fetchData("?sortingOrder=" + sorting + "&sortingProperty=username")
    }

    const tokenNumberSorting = () => {

        if (sorting === "desc") {
            setSorting("asc")
        }
        else {
            setSorting("desc")
        }

        fetchData("?sortingOrder=" + sorting + "&sortingProperty=tokenNumber")
    }

    let SearchIcon =
        <div title={"search"}>
            <Search size={28}
                onClick={() => setClicked(!clicked)}
                className={"search-button"}
            />
        </div>

    const handleSearch = () => {
        if (disable) {

        } else {
            setClicked(false);
            setSearch("");
            setsearchNumber("")
            fetchData("")
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

            <table onBlur={() => setClicked(false)}>

                <thead>

                    <tr>

                        <th onClick={usernameSorting}>username</th>
                        <th >password</th>
                        <th onClick={tokenNumberSorting}>token number</th>
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
                                    value={search}
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
                                <X size={28}
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


