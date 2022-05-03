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
            numberSearch();
        }, number);
        setTimer(timerID)
    }, [searchNumber]);


    const searchSubmit = () => {
        if (search === "") {
            fetchData("");
        } else {
            fetchData("?username=" + search);
        }
    }


    const numberSearch = () => {
        if (searchNumber === "") {
            fetchData("");
        } else {
            fetchData("?tokenNumber=" + searchNumber)
        }
    }

    let SearchIcon =
        <div title={"search"}>
            <Search size={28}
                onClick={() => setClicked(true)}
                className={"search-button"}
            />
        </div>



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

                        <th>username</th>
                        <th>password</th>
                        <th>token number</th>
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
                                    onChange={(e) => setSearch(e.target.value)}
                                />
                                <input
                                    type="search"
                                    className={"search-token"}
                                    value={searchNumber}
                                    onChange={(e) => setsearchNumber(e.target.value)}
                                />
                            </div>

                            <div title="Close">
                                <X size={28}
                                    className={"close-button"}
                                    onClick={() => {
                                        setClicked(false);
                                        setSearch("");
                                        setsearchNumber("")
                                    }}
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


