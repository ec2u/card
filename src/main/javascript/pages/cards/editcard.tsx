import { Check, Trash2, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Deletedialog } from "../users/deletedialog";
import { AddCard } from "./addcard";
import './editcard.css'

interface User {
    holderForename: string;
    holderSurname: string;
    expiringDate: Date;
    virtualCardNumber: number;
    label: string;
    id: any;
}

export function EditCard() {
    const [updatecard, setUpdatecard] = useState<User>({
        holderForename: "",
        holderSurname: "",
        virtualCardNumber: 0,
        expiringDate: new Date()

    } as User);

    const [dialog, setDialog] = useState<Boolean>(false);
    const [clicked, setClicked] = useState<Boolean>(false);
    const [disable, setDisable] = useState<Boolean>(false);
    const [loading, setLoading] = useState<Boolean>(false);

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            await fetch(`/cards/${id}`, {
                headers: {
                    Accept: "application/json",
                },
            })
                .then((response) => response.json())
                .then((data) => setUpdatecard(data));
        }
        fetchData()
    }, []);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value

        if (updatecard.holderForename === "" || value === "" ||
            updatecard.holderSurname === "" ||
            updatecard.expiringDate === null ||
            updatecard.virtualCardNumber === 0
        ) {
            setDisable(true)
        }

        else {
            setDisable(false)
        }

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



    const handleEdit = async () => {
        if (disable) {

        }
        else {

            setLoading(true)
            await fetch(`/cards/${id}`, {
                method: "PUT",
                headers: {
                    "Content-type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(userdata),
            })
                .then(() => {
                    setLoading(false)
                    navigate(`${updatecard.id}`)
                })
                .catch(error => console.error('error:', error))
        }

    };


    const handleDelete = (id: number) => {
        fetch(`${id}`, {
            method: "DELETE",
        })
            .then(data => navigate('/cards/'))
    };


    const Showpopup = () => {
        setDialog(!dialog);
    };



    let showCheck =

        <div title='Update' >
            {loading ? (<div className={"spinner"}></div>) :
                (<Check size={40} className={'check-button'}
                    onClick={handleEdit}
                    color={disable ? 'lightgray' : 'black'}
                />)}
        </div >


    let showTrash =
        <div>
            <label title='Delete'>
                <Trash2 size={40}
                    className={"trash-button"}
                    onClick={() => Showpopup()} />
            </label>
        </div>



    const handleonFocus = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.target.select();
        setClicked(true)
    }


    return createElement("card-editcard", {},
        <>
            <header>

                <section>
                    <a href='/cards/' className={"cards-link"} title="Cards"> Cards &#8250;</a>
                    <a href={`${updatecard.id}`}>{updatecard.label} </a>
                </section>
                <section>
                    <a> {clicked ? showCheck : showTrash}</a>
                    <a href={`${updatecard.id}`} title='Close'>
                        <X size={44} className={"close-button"} />
                    </a>
                </section>

            </header>

            <form>

                <div className={"start"}>

                    <section>
                        <label>forename</label>
                        <input
                            required
                            type='text'
                            name="holderForename"
                            className={"forename"}
                            value={updatecard.holderForename}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                    <section>
                        <label>surname</label>
                        <input
                            required
                            type='text'
                            name="holderSurname"
                            className={"surname"}
                            value={updatecard.holderSurname}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                    <section>
                        <label>expiry date</label>
                        <input
                            required
                            type='date'
                            className={"expiry-date"}
                            name="expiringDate"
                            value={updatecard.expiringDate}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                </div>

                <div>

                    <section>
                        <label>card number</label>
                        <input className={"card-number"}
                            required
                            name='virtualCardNumber'
                            type="text"
                            value={updatecard.virtualCardNumber}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                </div>

            </form>

            {dialog && (
                <Deletedialog
                    handleyes={() => handleDelete(updatecard.id)}
                    handleno={Showpopup}
                />
            )}
        </>
    );

}
