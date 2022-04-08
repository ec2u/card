import { setDefaultResultOrder } from 'dns';
import { ChevronRight, Plus, Search, X } from 'lucide-react';
import React, { createElement, useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom';
import './cards.css';


interface Card {

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
            await fetch('/cards/', {
                headers: {
                    Accept: 'application/json',
                }
            })
                .then(response => response.json())
                .then(data => setCards(data.contains))
                .catch((error) => console.warn("Error:", error))

            // error handle
            // .catch((error) => setError(error))

            setLoading(false)
        }
        fetchData();
    }, [])


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
                onChange={(e) => setSearch(e.target.value)}

            />
            <X size={20} color="black"
                onClick={() => setClicked(false)}
            />
        </div>

    let SearchIcon =
        <div title={"search"}>
            <Search size={28}
                className={'button-search'}
                onClick={() => setClicked(true)}
                onBlur={() => setClicked(false)}
            />
        </div>

    return createElement('card-cards', {},
        <>
            <header>

                <a>Cards</a>
                <a title="addcard" href='/cards/add'>
                    <Plus size={38} className={"button-plus"} />
                </a>

            </header>

            <table>
                <thead>
                    <tr>
                        <th>forename</th>
                        <th>surname</th>
                        <th>expiry date</th>
                        <th>card number</th>
                        <th>
                            {clicked ? searchInput : SearchIcon}

                        </th>
                    </tr>
                </thead>

                <hr />

                {loading ? (<div className={'spinner'}></div>) : (
                    <tbody>
                        {cards.filter(card => card.holderSurname.toLowerCase().includes(search.toLowerCase())).map((card) => {
                            return (
                                <tr key={card.id} >
                                    <td>{card.holderForename}</td>
                                    <td>{card.holderSurname}</td>
                                    <td>{card.expiringDate}</td>
                                    <td>{card.virtualCardNumber}</td>

                                    <td>
                                        <Link to={`${card.id}`} title="inspect">
                                            <ChevronRight size={40}
                                                className={"button-arrow"} />
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
