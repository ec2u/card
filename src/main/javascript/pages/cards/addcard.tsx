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


export function AddCard() {
    const [addcard, setAddcard] = useState({
        holderForename: "",
        holderSurname: "",
        id: "",
        virtualCardNumber: 0,
        expiringDate: new Date()
    } as Card);

    const [disable, setDisable] = useState<Boolean>(true);
    const [loading, setLoading] = useState<Boolean>(false);

    const navigate = useNavigate();

    let userData = {
        holderForename: addcard.holderForename,
        expiringDate: addcard.expiringDate,
        holderSurname: addcard.holderSurname,
        virtualCardNumber: addcard.virtualCardNumber,
    };


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {


        const value = e.target.value

        if (addcard.holderForename === "" || addcard.holderSurname === ""
            || addcard.virtualCardNumber === 0 || value === "") {
            setDisable(true)

        } else {
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
                body: JSON.stringify(userData),
            })
                .then(() => {
                    setLoading(false)
                    navigate('/cards/');

                })
                .catch(error => console.warn('error:', error));
        }
    }



    return createElement("card-addcard", {},
        <>
            <header>
                <section>
                    <a href="/cards/" className={"cards-link"} title="Cards">Cards </a>
                    <a>&#8250;</a>
                    <a> New Card</a>
                </section>

                <section>
                    <a title="Save">
                        {loading ? (<div className={"spinner"}></div>
                        ) : (
                            <Check onClick={handleSubmit}
                                size={38}
                                color={disable ? 'lightgray' : 'black'}
                                className={"button-check"}

                            />)}
                    </a>
                    <a href="/cards/" title="Close">
                        < X size={40} className={'button-close'} />
                    </a>
                </section>

            </header>

            <form>

                <div className={"start"}>

                    <section>
                        <label>forename</label>
                        <input
                            className={"input"}
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
                            className={"input"}
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
                            className={"input"}
                            type="date"
                            required
                            name="expiringDate"
                            onChange={handleChange}
                        // className={'expiringdate'}
                        />
                    </section>

                </div>

                <div className={"end"}>

                    <section>
                        <label>card number</label>
                        <input
                            className={"input"}
                            required
                            // className="number"
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
