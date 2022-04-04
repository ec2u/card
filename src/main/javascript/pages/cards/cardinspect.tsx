import { Edit, Trash } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
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




    return createElement("card-inspectcard", {},
        <>
            <header>
                <section>
                    <a href="/cards/">Cards &#62;</a>
                    <a>{card.label}</a>
                </section>

                <a href={`/edit${card.id}`}>
                    <Edit size={38} className="button-edit" />
                </a>
            </header>
            <form>
                <div className={"start"}>
                    <section>
                        <label>forename</label>
                        <span>{card.holderForename}</span>
                    </section>

                    <section>
                        <label>surname</label>
                        <span>{card.holderSurname}</span>
                    </section>

                    <section>
                        <label>expiry date</label>
                        <span>{card.expiringDate}</span>
                    </section>


                </div>

                <div>
                    <section>
                        <label>card number</label>
                        <span>{card.virtualCardNumber}</span>
                    </section>
                </div>
            </form>
        </>
    );

}
