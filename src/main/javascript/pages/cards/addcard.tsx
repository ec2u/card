import { Check, X } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./addcard.css";


interface Card {
    holderForename: string;
    holderSurName: string;
    expiringDate: Date;
    virtualCardNumber: number;
    id: any;

}


export function Addcard() {
    const [addcard, setAddcard] = useState({} as Card);
    const navigate = useNavigate();

    const userdata = {
        holderForename: addcard.holderForename,
        expiringDate: addcard.expiringDate,
        holderSurName: addcard.holderSurName,
        virtualCardNumber: addcard.virtualCardNumber,
    };


    const handleSubmit = (e: any) => {
        e.preventDefault();
        fetch("/cards/", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userdata),
        }).then((response) => response.json())
            .then(data => navigate('/users'))
    };



    const handleChange = (e: any) => {
        setAddcard((addcard) => ({
            ...addcard,
            [e.target.name]: e.target.value,
        }));
    };



    return (
        <div className="cards">

            <div className="topnav-addcard">

                <span> Add Card</span>

                <span>
                    <Check type='submit' onClick={handleSubmit} size={38} className="button-check" />
                </span>

                <span>
                    <Link to='/cards/'>
                        < X size={40} className={'button-close'} />
                    </Link>
                </span>

            </div>

            <div className="grid-container-addcard">


                <table>

                    <thead>

                        <tr className="tr-addcard">
                            <th>forename</th>
                            <th>surname</th>
                            <th>expiry date</th>

                            <th > card number</th>

                        </tr>

                    </thead>

                    <hr className="solid" />

                    <tbody>

                        <tr className="tr-addcard" >

                            <td>

                                <input
                                    type="text"
                                    required
                                    name="holderForename"
                                    value={addcard.holderForename}
                                    onChange={handleChange}
                                />

                            </td>


                            <td>

                                <input
                                    type="text"
                                    required
                                    name="holderSurName"
                                    value={addcard.holderSurName}
                                    onChange={handleChange}
                                />

                            </td>


                            <td>

                                <input
                                    type="Date"
                                    required
                                    name="expiringDate"
                                    value={addcard.expiringDate}
                                    onChange={handleChange}
                                    className={'expiringdate'}
                                />

                            </td>


                            <td>

                                <input
                                    className="number"
                                    type="text"
                                    name="virtualCardNumber"
                                    value={addcard.virtualCardNumber}
                                    onChange={handleChange}

                                />

                            </td>

                        </tr>


                    </tbody>

                </table>

            </div>

        </div>

    );
}
