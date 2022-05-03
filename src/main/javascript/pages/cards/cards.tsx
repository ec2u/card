
import { ChevronRight, Plus, Search, X, ChevronDown } from 'lucide-react';
import React, { createElement, useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import './cards.css';


interface Card {
    label: string;
    holderForename: string;
    holderSurname: string;
    expiringDate: Date;
    virtualCardNumber: number;
    id: any;
}


export function VirtualCards() {

    const [cards, setCards] = useState<Card[]>([]);
    const [loading, setLoading] = useState<Boolean>(false);
    const [error, setError] = useState<any>(null);
    const [clicked, setClicked] = useState<Boolean>(false);
    const [search, setSearch] = useState<string>("");
    const [searchDate, setSearchDate] = useState<any>("");
    const [searchNumber, setSearchNumber] = useState<string>("");
    const [timer, setTimer] = useState<number>(0);
    const [disable, setDisable] = useState<Boolean>(true)



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
        fetchData("")
    }, [])


    const searchForenameSubmit = (e: string) => {
        if (e === "") {
            fetchData("");
            setDisable(true)
        } else {
            fetchData("?label=" + e);
            setDisable(false)
        }
    }
    const handleSearchForenameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value)
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchForenameSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    const searchDateSubmit = (e: any) => {
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
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchNumberSubmit(e.target.value)
        }, 1000);
        setTimer(timerID)
    }

    let SearchIcon =
        <div title={"search"} className={"search-icon"}>
            <Search size={28} color="gray"
                className={'button-search'}
                onClick={() => setClicked(!clicked)}
            />
        </div>

    const handleSearch = () => {
        if (disable) {

        } else {
            setClicked(false);
            setSearch("")
            fetchData("")
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
            {/*  */}
            <table onBlur={() => setClicked(false)}>
                <thead>
                    <tr>
                        <th>
                            <a>forename</a>
                            <a><ChevronDown

                                className={"down-arrow"}
                            /></a>

                        </th>
                        <th>surname</th>
                        <th>expiry date</th>
                        <th>card number</th>
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
                                    value={search}
                                    onChange={handleSearchForenameChange}
                                />
                                <input
                                    className={"search-surname"}
                                    type="search"
                                />
                                <input
                                    type="search"
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
                                <X size={30}
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
