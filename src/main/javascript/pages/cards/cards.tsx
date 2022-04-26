
import { ChevronRight, Plus, Search, X } from 'lucide-react';
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
    const [search, setSearch] = useState<any>("");
    const [timer, setTimer] = useState<number>(0);



    const fetchData = async (searchdata: string) => {
        setLoading(true)

        await fetch(`/cards/` + searchdata, {
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
        let number = clicked ? 1000 : 1
        clearTimeout(timer)
        let timerID = window.setTimeout(() => {
            searchSubmit();
        }, number);
        setTimer(timerID)
    }, [search])


    const searchSubmit = () => {
        if (search === "") {
            fetchData("");
        } else {
            fetchData("filters?surnamePrefix=" + search);
        }
    }
    const inputRef = useRef<HTMLInputElement>(null);

    // const handleSearchLabelChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    //     setSearch(e.target.value)

    // }

    let SearchIcon =
        <div title={"search"} className={"search-icon"}>
            <Search size={28} color="gray"
                className={'button-search'}
                onClick={() => setClicked(true)}
            />
        </div>

    return createElement('card-cards', {},
        <>
            <header>

                <a>Cards</a>
                <a title="newcard" href='/cards/add'>
                    <Plus size={38} className={"button-plus"}
                    />
                </a>

            </header>
            {/*  onBlur={() => setClicked(false)}*/}
            <table  >
                <thead>
                    <tr>
                        <th>forename</th>
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
                                    className={"search-label"}
                                    value={search}
                                    onChange={(e) => setSearch(e.target.value)}
                                />
                                <input
                                    type="date"
                                    className={"search-date"}
                                />
                                <input
                                    className={"search-number"}
                                />
                            </div>
                            <div >
                                <X size={30}
                                    onClick={() => {
                                        setClicked(false);
                                        setSearch("")
                                    }}
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
