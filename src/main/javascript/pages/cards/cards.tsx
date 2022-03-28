import { ChevronRight, Plus, Search } from 'lucide-react';
import React, { createElement, useEffect, useState } from 'react'
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

            setLoading(false)
        }
        fetchData();
    }, [])


    return createElement('card-cards', {},
        <>
            < div className={"cards"} >
                <div className={"topnav-cards"}>
                    <span> Cards</span>
                    <a title="addcard" href='/cards/add'>

                        <Plus size={38} className={"button-plus"} />

                    </a>
                </div>

                <div className="grid-container-cards">
                    <table>
                        <thead>
                            <tr className='tr-cards'>
                                <th>forename</th>
                                <th>surname</th>
                                <th>expiry date</th>
                                <th>card number</th>
                                <th>
                                    <Search size={28} color="black" className='button-search' />
                                </th>


                            </tr>
                        </thead>

                        <hr className={"solid-cards"} />
                        {loading ? (<div className='spinner'></div>) : (
                            <tbody>
                                {cards.map((card) => {
                                    return (
                                        <tr key={card.id} className='tr-cards'>
                                            <td>{card.holderForename}</td>
                                            <td>{card.holderSurname}</td>
                                            <td>{card.expiringDate}</td>
                                            <td>{card.virtualCardNumber}</td>

                                            <td>
                                                <Link to={`${card.id}`}>
                                                    <ChevronRight size={40} className={"button-arrow"} />
                                                </Link>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>)}


                    </table>

                </div>
            </div >

        </>
    )




}
