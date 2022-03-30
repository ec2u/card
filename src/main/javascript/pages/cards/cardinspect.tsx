import { ChevronRight, Edit, Trash } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import "./cardinspect.css";

interface Card {
    holderForename: string;
    holderSurname: string;
    expiringDate: Date;
    virtualCardNumber: number;
    id: any;
    label: string;

}



export function CardInspect() {
    const [card, setCard] = useState<Card>({} as Card);
    const { id } = useParams();

    useEffect(() => {
        fetch(`/cards/${id}`, {
            headers: {
                Accept: "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setCard(data))
            .catch(error => console.warn('error:', error))


    }, [])




    return (
        <div className="cards">
            <div className="topnav-cardinspect">
                <span title="close"> <Link to='/cards/' className="cards-link"> Cards &gt;</Link></span>


                <span>{card.label}</span>
                <span title="edit">
                    <Link to={`/edit${card.id}`}>
                        <Edit size={38} className="button-plus" />
                    </Link>
                </span>
            </div>

            <div className="grid-container-cardinspect">
                <table>
                    <thead>
                        <tr className="tr-cardinspect">
                            <th>forename</th>
                            <th>surname</th>
                            <th>expiry date</th>

                            <th> card number </th>
                        </tr>
                    </thead>

                    <hr className="solid" />

                    <tbody>
                        <tr className="tr-cardinspect" key={card.id}>
                            <td>{card.holderForename}</td>
                            <td>{card.holderSurname}</td>
                            <td>{card.expiringDate}</td>
                            <td >

                                {card.virtualCardNumber}

                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
}
