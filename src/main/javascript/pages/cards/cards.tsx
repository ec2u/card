
import { ChevronRight, Plus, Search, X } from 'lucide-react';
import React, { createElement, useEffect, useRef, useState } from 'react'
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
    const [search, setSearch] = useState<string>("")



    useEffect(() => {
        const fetchData = async () => {
            setLoading(true)
            
            let path = '/cards/filter/'

            await fetch('/cards/', {
                headers: {
                    Accept: 'application/json',
                }
            })
                .then(response => response.json())
                .then(data => setCards(data.contains))
                .catch((error) => setError(error))

            // error handle
            // .catch((error) => setError(error))

            setLoading(false)
        }
        fetchData();
    }, [])




    const inputRef = useRef<HTMLInputElement>(null);

    // let searchInput =
    //     <div className={"search-box"}
    //         onSubmit={(e) => inputRef.current?.blur()
    //         }
    //     >

    //         <input
    //             ref={inputRef}
    //             type="search"
    //             autoFocus
    //             value={search}
    //             placeholder="search..."
    //             onChange={(e) => setSearch(e.target.value)}

    //         />

    //     </div>


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

            <table onBlur={() => setClicked(false)}>
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
                        <div className={"search-fields"}><input
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
                            <X size={30} color="black"
                                onClick={() => setClicked(false)}
                                className={"close-button"}
                            />
                        </div>) : ("")}

                </caption>

                {loading ? (<caption className={'spinner'}></caption>) : (
                    <tbody>
                        {/* {cards.filter(card => card.expiringDate.includes(search))} */}
                        {cards.filter(card => card.label.toLowerCase().includes(search.toLowerCase())).map((card) => {
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
