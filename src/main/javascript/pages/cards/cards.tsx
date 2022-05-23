
import { ChevronRight, Plus, Search, XCircle, ChevronDown, ChevronUp } from 'lucide-react';
import React, { createElement, useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import './cards.css';


interface Card {
    label: string;
    holderForename: string;
    holderSurname: string;
    expiringDate: Date;
    virtualCardNumber: number;
    id: number | string;
}


export function VirtualCards() {
    let searchForename_data = sessionStorage.getItem('searchForenameCard');
    let searchSurname_data = sessionStorage.getItem('searchSurnameCard');
    let searchExpiryDate_data = sessionStorage.getItem('searchExpiryDateCard');
    let searchCardNumber_data = sessionStorage.getItem('searchCardNumber');
    const [cards, setCards] = useState<Card[]>([]);
    const [loading, setLoading] = useState<Boolean>(false);
    const [error, setError] = useState<any>(null);
    const [clicked, setClicked] = useState<Boolean>(false);
    const [searchForename, setSearchForename] = useState<string>(searchForename_data != null ? searchForename_data : "");
    const [searchSurname, setSearchSurname] = useState<string>(searchSurname_data != null ? searchSurname_data : "");
    const [searchDate, setSearchDate] = useState<any>(searchExpiryDate_data != null ? searchExpiryDate_data : "");
    const [searchNumber, setSearchNumber] = useState<any>(searchCardNumber_data != null ? searchCardNumber_data : "");
    const [timer, setTimer] = useState<number>(0);
    const [disable, setDisable] = useState<Boolean>(true)

    const [sorting, setSorting] = useState<string>("asc");
    const [sortingType, setSortingType] = useState<string>("")


    const fetchData = async (searchData: any) => {
        setLoading(true)

        await fetch(`/cards/` + searchData, {
            headers: {
                Accept: 'application/json',
            }
        })
            .then(response => response.json())
            .then(data => setCards(data.contains))
            .catch((error) => setError(error))

        setLoading(false)
    }

    useEffect(() => {
        let search = sessionStorage.getItem('searching')
        if (search) {
            setClicked(true)
            switch (search) {
                case "forename":
                    searchForenameSubmit(searchForename);
                    break;
                case "surname":
                    searchSurnameSubmit(searchSurname);
                    break;
                case "expiringDate":
                    searchDateSubmit(searchDate);
                    break;
                case "cardnumber":
                    searchDateSubmit(searchNumber);
                    break;
            }
            sessionStorage.removeItem('searching')
        } else
            fetchData("")
    }, [])


    const searchForenameSubmit = (e: string) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?forename=" + e);
            setDisable(false)
        }
    }
    const handleSearchForenameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchForename(e.target.value)
        sessionStorage.setItem('searchForenameCard', e.target.value);
        sessionStorage.setItem('searching', "forename")
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchForenameSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const searchSurnameSubmit = (e: string) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?surname=" + e);
            setDisable(false)
        }
    }
    const handleSearchSurnameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchSurname(e.target.value)
        sessionStorage.setItem('searchSurnameCard', e.target.value);
        sessionStorage.setItem('searching', "surname")
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchSurnameSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const searchDateSubmit = (e: string) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?expiringDate=" + e);
            setDisable(false)
        }
    }

    const handleSearchDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchDate(e.target.value)
        sessionStorage.setItem('searchExpiryDateCard', e.target.value);
        sessionStorage.setItem('searching', "expiringDate")
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchDateSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const searchNumberSubmit = (e: any) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?virtualCardNumber=" + e);
            setDisable(false)
        }
    }

    const handleSearchNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchNumber(e.target.value)
        sessionStorage.setItem('searchCardNumber', e.target.value);
        sessionStorage.setItem('searching', "cardnumber")
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchNumberSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }


    const forenameSorting = () => {
        setSearchForename("")
        setSortingType("forename")
        if (sorting === "asc") {
            let sort = "asc"
            fetchData("?sortingOrder=" + sort + "&sortingProperty=holderForenameLowerCase")

            setSorting("desc")

        } else if (sorting === "desc") {
            let sort = "desc"
            setSorting("")
            fetchData("?sortingOrder=" + sort + "&sortingProperty=holderForenameLowerCase")

        } else {
            setSorting("asc")
            fetchData("")
        }
    }

    const surnameSorting = () => {
        setSearchSurname("")
        setSortingType("surname")
        if (sorting === "asc") {
            let sort = "asc"
            fetchData("?sortingOrder=" + sort + "&sortingProperty=holderSurnameLowerCase")

            setSorting("desc")

        } else if (sorting === "desc") {
            let sort = "desc"
            setSorting("")
            fetchData("?sortingOrder=" + sort + "&sortingProperty=holderSurnameLowerCase")

        } else {
            setSorting("asc")
            fetchData("")
        }
    }

    const expiryDateSorting = () => {
        setSearchDate("")
        setSortingType("expiringDate")

        if (sorting === "asc") {
            let sort = "asc"
            fetchData("?sortingOrder=" + sort + "&sortingProperty=expiringDate")

            setSorting("desc")

        } else if (sorting === "desc") {
            let sort = "desc"
            setSorting("")
            fetchData("?sortingOrder=" + sort + "&sortingProperty=expiringDate")

        } else {
            setSorting("asc")
            fetchData("")
        }
    }

    const numberSorting = () => {
        setSearchNumber("")
        setSortingType("virtualCardNumber")
        if (sorting === "asc") {
            let sort = "asc"
            fetchData("?sortingOrder=" + sort + "&sortingProperty=virtualCardNumber")

            setSorting("desc")

        } else if (sorting === "desc") {
            let sort = "desc"
            setSorting("")
            fetchData("?sortingOrder=" + sort + "&sortingProperty=virtualCardNumber")

        } else {
            setSorting("asc")
            fetchData("")
        }
    }





    let SearchIcon =
        <div title={"search"} className={"button-search"}>
            <Search size={28}
                className={'button-search'}
                onClick={() => {
                    setClicked(!clicked)
                    setSearchForename("");
                    setSearchSurname("");
                    setSearchNumber("")
                    fetchData("")
                    sessionStorage.removeItem('searchForenameCard');
                    sessionStorage.removeItem('searchSurnameCard');
                    sessionStorage.removeItem('searchExpiryDateCard');
                    sessionStorage.removeItem('searchCardNumber');
                }}
            />
        </div>

    const handleSearch = () => {
        if (disable) {

        } else {
            setClicked(false);
            setSearchForename("");
            setSearchSurname("");
            setSearchNumber("")
            fetchData("")
            sessionStorage.removeItem('searchForenameCard');
            sessionStorage.removeItem('searchSurnameCard');
            sessionStorage.removeItem('searchEmailCard');
            sessionStorage.removeItem('searchCardNumber');
        }
    }

    return createElement('card-cards', {},
        <>
            <header>

                <a>Cards</a>
                <a title="newcard" href='/cards/add'>
                    <Plus size={38} className={"button-plus"}
                    />
                </a>

            </header>

            <table>
                <thead>
                    <tr>
                        <th onClick={forenameSorting}>forename
                            {sortingType === "forename" ? sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown /> : ""}
                        </th>
                        <th onClick={surnameSorting} >surname
                            {sortingType === "surname" ? sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown /> : ""}
                        </th>
                        <th onClick={expiryDateSorting}> expiry date
                            {sortingType === "expiringDate" ? sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown /> : ""}
                        </th>
                        <th onClick={numberSorting}>card number
                            {sortingType === "virtualCardNumber" ? sorting ? sorting === "asc" ? "" : <ChevronUp /> : <ChevronDown /> : ""}
                        </th>
                        <th>
                            {SearchIcon}
                        </th>
                    </tr>
                </thead>

                <caption>  <hr /> </caption>
                <caption >
                    {clicked ? (
                        <div className={"search-fields"}>
                            <div className={"search-fields-start"}>
                                <input
                                    type="search"
                                    className={"search-forename"}
                                    value={searchForename}
                                    onChange={handleSearchForenameChange}

                                />
                                <input
                                    className={"search-surname"}
                                    type="search"
                                    value={searchSurname}
                                    onChange={handleSearchSurnameChange}
                                />
                                <input
                                    type="date"
                                    className={"search-date"}
                                    value={searchDate}
                                    onChange={handleSearchDateChange}
                                />
                                <input
                                    type="search"
                                    className={"search-number"}
                                    value={searchNumber}
                                    onChange={handleSearchNumberChange}
                                    pattern="[0-9]*"
                                />
                            </div>
                            <div >
                                <XCircle size={30}
                                    color={disable ? 'lightgray' : 'black'}
                                    onMouseDown={handleSearch}
                                    className={"close-button"}
                                />
                            </div>
                        </div>
                    ) : ("")}

                </caption>

                {loading ? (<caption className={'spinner'}></caption>) : (
                    <tbody >

                        {cards.map((card) => {
                            return (
                                <tr key={card.id} >
                                    <td>{card.holderForename}</td>
                                    <td>{card.holderSurname}</td>
                                    <td>{card.expiringDate}</td>
                                    <td>{card.virtualCardNumber}</td>

                                    <td>
                                        <Link to={`${card.id}`} title="inspect">
                                            <ChevronRight size={40}
                                                className={"button-arrow"}
                                            />
                                        </Link>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>)}
            </table>
        </>
    );
}
