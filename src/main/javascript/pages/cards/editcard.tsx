import { Check, Trash2, X } from "lucide-react";
import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { Deletedialog } from "../users/deletedialog";
import './editcard.css'

interface User {
    holderForename: string;
    holderSurname: string;
    expiringDate: Date;
    virtualCardNumber: number;
    label: string;
    id: any;
}

export function Editcard() {
    const [updatecard, setUpdatecard] = useState<User>({} as User);
    const [dialog, setDialog] = useState<Boolean>(false);
    const [clicked, setClicked] = useState<Boolean>(false);
    const [disable, setDisable] = useState<Boolean>(false)

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
        holderSurname: updatecard.holderSurname,
        expiringDate: updatecard.expiringDate,
        id: updatecard.id,
        label: updatecard.label
    };



    const handleEdit = () => {
        if (disable) {

        } else {
            fetch(`/cards/${id}`, {
                method: "PUT",
                headers: {
                    "Content-type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(userdata),
            }).then((response) => response.json());
        }

    };

    const handleDelete = (id: any) => {
        fetch(`${id}`, {
            method: "DELETE",
        })
            .then(data => navigate('/cards/'))
    };

    const Showpopup = () => {
        setDialog(!dialog);
    };

    let showCheck =
        <div>
            < a title='Update' href={`${updatecard.id}`} >
                <Check size={40} className={'check-button'} onClick={handleEdit} color={disable ? 'lightgray' : 'black'} />
            </a >
        </div>

    let showTrash =
        <div>
            <label title='Delete'>
                <Trash2 size={40} className={"trash-button"} onClick={(e) => Showpopup()} />
            </label>
        </div>

    const handleonFocus = () => {
        setClicked(true)
    }


    return (
        <div>
            <div className={"cards"}>
                <div className={"topnav-cardedit"}>
                    <div className="topnav-start">
                        <div>
                            <a href='/cards/'> Cards &gt;</a>
                        </div>

                        <div>
                            <a href={`${updatecard.id}`}>{updatecard.label}
                            </a>
                        </div>
                    </div>
                    <div>

                        <div className="navend">
                            <label>
                                {clicked ? showCheck : showTrash}
                            </label>
                            <a href={`${updatecard.id}`} title='Close'>
                                <X size={42} className={"button-trash"} />
                            </a>
                        </div>
                    </div>


                </div>

                <form >


                    <div className="data-edit">
                        <div className="data-start">

                            <div className="data-section">
                                <label>forename</label>

                                <input
                                    required
                                    type='text'
                                    name="holderForename"
                                    className="forename"
                                    value={updatecard.holderForename}
                                    onChange={handleChange}
                                    onFocus={handleonFocus}
                                // onBlur={handleonBlur}
                                />
                            </div>


                            <div className="data-section">
                                <label>surname</label>

                                <input
                                    required
                                    type='text'
                                    name="holderSurname"
                                    className="surname"
                                    value={updatecard.holderSurname}
                                    onChange={handleChange}
                                    onFocus={handleonFocus}
                                // onBlur={handleonBlur}
                                />
                            </div>

                            <div className="data-section">
                                <label>email</label>

                                <input
                                    required
                                    type='date'
                                    className="date"
                                    name="expiringDate"
                                    value={updatecard.expiringDate}
                                    onChange={handleChange}
                                    onFocus={handleonFocus}
                                // onBlur={handleonBlur}
                                />
                            </div>

                        </div>

                        <div className="data-end">
                            <div className="end">
                                <label className="label-number"> card number</label>

                                <input className="number" required name='virtualCardNumber' type="text" value={updatecard.virtualCardNumber} onChange={handleChange} onFocus={handleonFocus}
                                // onBlur={handleonBlur}
                                />
                            </div>

                        </div>

                    </div>


                </form>


            </div>
            {
                dialog && (
                    <Deletedialog
                        handleyes={() => handleDelete(updatecard.id)}
                        handleno={Showpopup}
                    />
                )
            }
        </div >
    );
}
