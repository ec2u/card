import { Check, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./addcard.css";


interface Card {
    holderForename: string;
    holderSurname: string;
    expiringDate: Date;
    virtualCardNumber: number;
    id: number | string;

}


export function Addcard() {
    const [addcard, setAddcard] = useState({} as Card);
    const [disable, setDisable] = useState<Boolean>(false)
    const [loading, setLoading] = useState<Boolean>(false)
    const navigate = useNavigate();

    const userdata = {
        holderForename: addcard.holderForename,
        expiringDate: addcard.expiringDate,
        holderSurname: addcard.holderSurname,
        virtualCardNumber: addcard.virtualCardNumber,
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        const value = e.target.value

        if (!addcard.holderForename) { setDisable(true) }


        else {
            setDisable(false)
        }

        setAddcard((addcard) => ({
            ...addcard,
            [e.target.name]: value,
        }));
    };

    const handleSubmit = async () => {
        if (disable) {

        } else {

            setLoading(true)
            await fetch("/cards/", {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(userdata),
            })
                .then((response) => response.json())
                .catch(error => console.warn('error:', error));

            navigate('/cards/');
        }
    }



    return createElement("card-addcard", {},
        <>
            <header>
                <section>
                    <a href="/cards/" className={"cards-link"}>Cards </a>
                    <a>&#8250;</a>
                    <a> New Card</a>
                </section>

                <section>
                    <a >
                        {loading ? (<div className={"spinner"}></div>
                        ) : (
                            <Check onClick={handleSubmit}
                                size={38}
                                color={disable ? 'lightgray' : 'black'}
                                className={"button-check"}
                            />)}
                    </a>
                    <a href="/cards/">
                        < X size={40} className={'button-close'} />
                    </a>
                </section>

            </header>

            <form>

                <div className={"start"}>

                    <section>
                        <label>forename</label>
                        <input
                            type="text"
                            required
                            name="holderForename"
                            value={addcard.holderForename}
                            onChange={handleChange}
                        />
                    </section>

                    <section>
                        <label>surname</label>
                        <input
                            type="text"
                            required
                            name="holderSurname"
                            value={addcard.holderSurname}
                            onChange={handleChange}
                        />
                    </section>

                    <section>
                        <label>expiry date</label>
                        <input
                            type="Date"
                            required
                            name="expiringDate"
                            onChange={handleChange}
                            className={'expiringdate'}
                        />
                    </section>

                </div>

                <div className={"end"}>

                    <section>
                        <label>card number</label>
                        <input
                            required
                            className="number"
                            type="text"
                            name="virtualCardNumber"
                            value={addcard.virtualCardNumber}
                            onChange={handleChange}
                        />
                    </section>

                </div>

            </form>
        </>
    );
}
