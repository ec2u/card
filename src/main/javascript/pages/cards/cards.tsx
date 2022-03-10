import { ChevronRight, Plus, Search } from 'lucide-react';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import './cards.css';


interface Card {

    holderForename: string;
    holderSurName: string;
    expiringDate: Date;
    virtualCardNumber: number;
    id: any;


}


export function VirtualCards() {

    const [cards, setCards] = useState<Card[]>([]);


    useEffect(() => {
        fetch('/cards/', {
            headers: {
                Accept: 'application/json',
            }
        })
            .then(response => response.json())
            .then(data => setCards(data.contains))
    }, [])

    return (

        <div className={"cards"}>
            <div className={"topnav-cards"}>
                <span> Cards</span>
                <span>
                    <Link to="/cards/add">
                        <Plus size={38} className={"button-plus"} />
                    </Link>
                </span>
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

                    <tbody>
                        {cards.map((card) => {
                            return (
                                <tr key={card.id} className='tr-cards'>
                                    <td>{card.holderForename}</td>
                                    <td>{card.holderSurName}</td>
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
                    </tbody>
                </table>
            </div>
        </div>

    )
}
