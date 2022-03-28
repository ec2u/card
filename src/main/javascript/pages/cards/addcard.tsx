import { Check, X } from "lucide-react";
import { traceDeprecation } from "process";
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
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
    const [loading, setLoading] = useState<Boolean>(false)
    const navigate = useNavigate();

    const userdata = {
        holderForename: addcard.holderForename,
        expiringDate: addcard.expiringDate,
        holderSurname: addcard.holderSurname,
        virtualCardNumber: addcard.virtualCardNumber,
    };


    const handleSubmit = async () => {
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
        console.log('end of response')
        navigate('/cards/');
        console.log('end of handleSubmit');


    }






    const handleChange = (e: any) => {
        setAddcard((addcard) => ({
            ...addcard,
            [e.target.name]: e.target.value,
        }));
    };



    // let showCheck = <span>

    // </span>
    // let handleSpin =
    //     <div className="spinner"></div>


    return (
        <div className="cards">

            <div className="topnav-addcard">

                <span> Add Card</span>
                <span>
                    <a >
                        {loading ? (<div className="spinner"></div>) : (<Check onClick={handleSubmit} size={38} className="button-check" />)}
                    </a>
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
                                    name="holderSurname"
                                    value={addcard.holderSurname}
                                    onChange={handleChange}
                                />

                            </td>


                            <td>

                                <input
                                    type="Date"
                                    required
                                    name="expiringDate"
                                    onChange={handleChange}
                                    className={'expiringdate'}
                                />

                            </td>


                            <td>

                                <input
                                    required
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
