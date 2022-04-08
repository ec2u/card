import { Check, Trash2, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
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
    const [disable, setDisable] = useState<Boolean>(false);
    const [loading, setLoading] = useState<Boolean>(false);

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

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value

        if (value === "") { setDisable(true) }

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



    const handleEdit = () => {


        if (disable) {


        }

        else {

            setLoading(true)
            fetch(`/cards/${id}`, {
                method: "PUT",
                headers: {
                    "Content-type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(userdata),
            })
                .then((response) => response.json())
                .catch(error => console.warn('error:', error))
            setLoading(false)
            navigate(`${updatecard.id}`)
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

        < a title='Update' >
            {loading ? (<div className={"spinner"}></div>) :
                (<Check size={40} className={'check-button'}
                    onClick={handleEdit}
                    color={disable ? 'lightgray' : 'black'}
                />)}
        </a >


    let showTrash =
        <div>
            <label title='Delete'>
                <Trash2 size={40}
                    className={"trash-button"}
                    onClick={() => Showpopup()} />
            </label>
        </div>



    const handleonFocus = () => {
        setClicked(true)
    }


    return createElement("card-editcard", {},
        <>
            <header>

                <section>
                    <a href='/cards/' className={"cards-link"}> Cards &#8250;</a>
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
                            className="forename"
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
                            className="surname"
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
                            className="date"
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
                        <input className="number"
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
