import { Check, ChevronRight, Trash, X } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { Dialog } from "../users/dialog";
import "../users/dialog.css";
import './editcard.css'

interface User {
    holderForename: string;
    holderSurName: string;
    expiringDate: Date;
    virtualCardNumber: number;
    label: string;
    id: any;
}

export function Editcard() {
    const [updatecard, setUpdatecard] = useState<User>({} as User);
    const [dialog, setDialog] = useState<Boolean>(false);
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`/cards/${id}`, {
            headers: {
                Accept: "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setUpdatecard(data));
    }, []);

    const handleChange = (event: any) => {
        setUpdatecard((updatecard) => ({
            ...updatecard,
            [event.target.name]: event.target.value,
        }));
    };

    const userdata = {
        virtualCardNumber: updatecard.virtualCardNumber,
        holderForename: updatecard.holderForename,
        holderSurName: updatecard.holderSurName,
        expiringDate: updatecard.expiringDate,
        id: updatecard.id,
        label: updatecard.label
    };



    const handleEdit = () => {
        fetch(`/cards/${id}`, {
            method: "PUT",
            headers: {
                "Content-type": "application/json",
                Accept: "application/json",
            },
            body: JSON.stringify(userdata),
        }).then((response) => response.json());
    };

    const handleDelete = (id: any) => {
        fetch(`${id}`, {
            method: "DELETE",
        })
            .then(data => navigate('/users/'))
    };

    const Showpopup = () => {
        setDialog(!dialog);
    };

    return (
        <div>
            <div className={"cards"}>
                <div className={"topnav-cardedit"}>
                    <div> <Link to='/cards/' className="cards-link"> Cards</Link></div>
                    <div>
                        <ChevronRight size={35} />
                    </div>

                    <div>{updatecard.holderSurName}</div>
                    <div>
                        <Link to={`${updatecard.id}`}>
                            < Check size={40} onClick={handleEdit} className='button-check' />
                        </Link>

                    </div>
                    <div>
                        <Trash size={38} className={"button-trash"} onClick={Showpopup} />
                    </div>
                    <div>
                        <Link to={`${updatecard.id}`}>
                            <X size={42} className={"button-trash"} />
                        </Link>
                    </div>
                </div>

                <div className="grid-container-cardedit">
                    <table>
                        <thead>
                            <tr className="tr-cardedit">
                                <th>forename</th>
                                <th>surname</th>
                                <th>expiry date</th>
                                <th>label</th>
                                <th>id</th>

                                <th > card number</th>
                            </tr>
                        </thead>

                        <hr className={"solid-cardedit"} />

                        <tbody>
                            <tr className="tr-cardedit">
                                <td>
                                    <input
                                        name="holderForename"
                                        value={updatecard.holderForename}
                                        onChange={handleChange}
                                    />
                                </td>
                                <td>
                                    <input
                                        name="holderSurName"
                                        value={updatecard.holderSurName}
                                        onChange={handleChange}
                                    />
                                </td>
                                <td>
                                    <input
                                        type='text'
                                        className="expiringDate"
                                        name="expiringDate"
                                        value={updatecard.expiringDate}
                                        onChange={handleChange}
                                    />
                                </td>
                                <td>
                                    <input
                                        disabled
                                        className="label"
                                        name="label"
                                        value={updatecard.label}
                                        onChange={handleChange}
                                    />
                                </td>
                                <td>
                                    <input
                                        disabled
                                        className="id"
                                        name="id"
                                        value={updatecard.id}
                                        onChange={handleChange}
                                    />
                                </td>
                                <td>
                                    <input
                                        className="virtualCardNumber"
                                        name="virtualCardNumber"
                                        value={updatecard.virtualCardNumber}
                                        onChange={handleChange}
                                    />
                                </td>

                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            {dialog && (
                <Dialog
                    handleyes={() => handleDelete(updatecard.id)}
                    handleno={Showpopup}
                />
            )}
        </div>
    );
}
