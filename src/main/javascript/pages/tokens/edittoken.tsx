import { Check, Trash2, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Deletedialog } from "../users/deletedialog";
import './edittoken.css'

interface Token {
    readonly label: string;
    readonly serviceOrUserName: string;
    readonly serviceOrUserPassword: string;
    readonly id: number;
    readonly tokenNumber: number;
}

export function Edittoken() {
    const [updatetoken, setUpdatetoken] = useState<Token>({} as Token);
    const [dialog, setDialog] = useState<Boolean>(false);
    const [clicked, setClicked] = useState<Boolean>(false);
    const [disable, setDisable] = useState<Boolean>(false);
    const [loading, setLoading] = useState<Boolean>(false);

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`/tokens/${id}`, {
            headers: {
                Accept: "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setUpdatetoken(data));
    }, []);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value

        if (value === "") { setDisable(true) }

        else {
            setDisable(false)
        }

        setUpdatetoken((updatetoken) => ({
            ...updatetoken,
            [event.target.name]: event.target.value,
        }));
    };

    const tokendata = {
        serviceOrUserName: updatetoken.serviceOrUserName,
        serviceOrUserPassword: updatetoken.serviceOrUserPassword,
        tokenNumber: updatetoken.tokenNumber,
        id: updatetoken.id,
        label: updatetoken.label
    };



    const handleEdit = () => {


        if (disable) {


        }

        else {

            setLoading(true)
            fetch(`/tokens/${id}`, {
                method: "PUT",
                headers: {
                    "Content-type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(tokendata),
            })
                .then((response) => response.json())
                .catch(error => console.warn('error:', error))
            setLoading(false)
            navigate(`${updatetoken.id}`)
        }

    };


    const handleDelete = (id: number) => {
        fetch(`${id}`, {
            method: "DELETE",
        })
            .then(data => navigate('/tokens/'))
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



    const handleonFocus = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.target.select();
        setClicked(true)
    }


    return createElement("card-edittoken", {},
        <>
            <header>

                <section>
                    <a href='/tokens/' className={"tokens-link"}> Tokens &#8250;</a>
                    <a href={`${updatetoken.id}`}>{updatetoken.label} </a>
                </section>
                <section>
                    <a> {clicked ? showCheck : showTrash}</a>
                    <a href={`${updatetoken.id}`} title='Close'>
                        <X size={44} className={"close-button"} />
                    </a>
                </section>

            </header>

            <form>

                <div className={"start"}>

                    <section>
                        <label>username</label>
                        <input
                            required
                            type='text'
                            name="serviceOrUserName"
                            className="username"
                            value={updatetoken.serviceOrUserName}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                    <section>
                        <label>password</label>
                        <input
                            required
                            type='text'
                            name="serviceOrUserPassword"
                            className="password"
                            value={updatetoken.serviceOrUserPassword}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>



                </div>

                <div>

                    <section>
                        <label>token number</label>
                        <input
                            required
                            type='text'
                            className="tokennumber"
                            name="tokenNumber"
                            value={updatetoken.tokenNumber}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                </div>

            </form>

            {dialog && (
                <Deletedialog
                    handleyes={() => handleDelete(updatetoken.id)}
                    handleno={Showpopup}
                />
            )}
        </>
    );

}
