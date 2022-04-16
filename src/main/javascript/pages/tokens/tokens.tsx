import { ChevronRight, Plus, Search, X } from "lucide-react";
import React, { createElement, useCallback, useEffect, useRef, useState } from "react";
import './tokens.css';



interface Token {
    readonly label: string;
    readonly serviceOrUserName: string;
    readonly serviceOrUserPassword: string;
    readonly id: number | string;
    readonly tokenNumber: number;
}

export function CardTokens() {
    const [tokens, setTokens] = useState<Token[]>([]);
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
            await fetch("/tokens/", {
                headers: {
                    Accept: "application/json",
                },
            })
                .then((response) => response.json())
                .then((data) => setTokens(data.contains))
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
                autoFocus
                type="search"
                value={search}
                placeholder="search..."
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
                            {clicked ? searchInput : SearchIcon}

                        </th>

                    </tr>

                </thead>
                <caption><hr /></caption>


                {loading ?

                    (<caption className="spinner"></caption>

                    ) : (

                        <tbody>
                            {tokens.filter(token => token.serviceOrUserName.toLowerCase().includes(search.toLowerCase())).map((token) => {

                                return (
                                    <tr key={token.id} >

                                        <td>{token.serviceOrUserName}</td>
                                        <td>{token.serviceOrUserPassword}</td>
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


