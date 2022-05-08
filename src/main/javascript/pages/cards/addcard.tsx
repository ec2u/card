import { Check, X } from "lucide-react";
import { listenerCount } from "process";
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

const newDate = () => {
    var today, dd, mm, yyyy;
    today = new Date();
    dd = today.getDate() + 1;
    mm = today.getMonth() + 1;
    yyyy = today.getFullYear();

    if (mm < 10)
        mm = '0' + mm.toString();
    if (dd < 10)
        dd = '0' + dd.toString();

    return yyyy + "-" + mm + "-" + dd
}


export function AddCard() {


    const [addcard, setAddcard] = useState({
        holderForename: "",
        holderSurname: "",
        id: "",
        virtualCardNumber: 0,
        expiringDate: new Date
    } as Card);

    const [disable, setDisable] = useState<Boolean>(true);
    const [loading, setLoading] = useState<Boolean>(false);
    const [apiErrors, setapiErrors] = useState<any>([]);
    const [numberError, setNumberError] = useState<string>("");

    const navigate = useNavigate();

    let userData = {
        holderForename: addcard.holderForename,
        expiringDate: addcard.expiringDate,
        holderSurname: addcard.holderSurname,
        virtualCardNumber: addcard.virtualCardNumber,
    };


    const validateNumber = (e: number) => {

        if (/^\d+$/.test(String(e).toLowerCase())) {
            setNumberError("Valid number")
            return true;
        } else {
            setNumberError("invalid number")
            return (false)
        }

    }
    let disableDates = () => {
        var today, dd, mm, yyyy;
        today = new Date();
        dd = today.getDate() + 1;
        mm = today.getMonth() + 1;
        yyyy = today.getFullYear();

        if (mm < 10)
            mm = '0' + mm.toString();
        if (dd < 10)
            dd = '0' + dd.toString();

        return yyyy + "-" + mm + "-" + dd

    }


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        const value = e.target.value

        if (addcard.holderForename === "" || addcard.holderSurname === "" ||
            addcard.virtualCardNumber === 0 ||
            value === "" ||
            ((e.target.name = "virtualCardNumber") && !validateNumber(addcard.virtualCardNumber)) ||
            addcard.expiringDate === null
        ) {
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
                .then((response) => {
                    setLoading(false)
                    if (response.ok) {
                        navigate("/cards/")
                    } else {
                        setapiErrors(response.status + "\n" + response.statusText);
                        window.alert("Something Went Wrong. !!!" + "\n" + response.status + "\n" + response.statusText)

                    }
                })
                .catch(error => console.warn("error:", error))
        }

    }


    return createElement("card-addcard", {},
        <>
            <header>
                <section>
                    <a href="/cards/" className={"cards-link"} title="Cards">Cards &#8250;</a>
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
                            className={"forename"}
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
                            className={"surname"}
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
                            className={"expiry-date"}
                            type="date"
                            value={addcard.expiringDate}
                            required
                            name="expiringDate"
                            onChange={handleChange}
                            min={disableDates()}
                        />

                    </section>


                </div>

                <div className={"end"}>

                    <section>
                        <label>card number</label>
                        <input
                            className={"card-number"}
                            required
                            type="text"
                            name="virtualCardNumber"
                            value={addcard.virtualCardNumber}
                            onChange={handleChange}
                            pattern="[0-9]*"
                        />
                        <span>{numberError}</span>
                    </section>

                </div>

            </form>
            <dialog >
                {apiErrors}
            </dialog>
        </>
    );
}
