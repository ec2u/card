import { Check, Trash2, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Deletedialog } from "../users/deletedialog";
import './edittoken.css'

interface Token {
    readonly label: string;
    readonly username: string;
    readonly password: string;
    readonly id: number;
    readonly tokenNumber: number;
}

export function EditToken() {
    const [updatetoken, setUpdatetoken] = useState<Token>({
        label: "",
        username: "",
        password: "",
        tokenNumber: 0,
    } as Token);
    const [dialog, setDialog] = useState<Boolean>(false);
    const [clicked, setClicked] = useState<Boolean>(false);
    const [disable, setDisable] = useState<Boolean>(true);
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

        if (value === "" || updatetoken.username === "" ||
            updatetoken.password === "" ||
            updatetoken.tokenNumber === 0) { setDisable(true) }

        else {
            setDisable(false)
        }

        setUpdatetoken((updatetoken) => ({
            ...updatetoken,
            [event.target.name]: event.target.value,
        }));
    };

    const tokenData = {
        username: updatetoken.username,
        password: updatetoken.password,
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
                body: JSON.stringify(tokenData),
            })
                .then(() => {
                    setLoading(false)
                    navigate(`${updatetoken.id}`)
                })
                .catch(error => console.warn('error:', error))

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

        < div title='Update' >
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


    return createElement("card-edittoken", {},
        <>
            <header>

                <section>
                    <a href='/tokens/' className={"tokens-link"} title="Tokens"> Tokens &#8250;</a>
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
                            name="username"
                            className={"username"}
                            value={updatetoken.username}
                            onChange={handleChange}
                            onFocus={handleonFocus}
                        />
                    </section>

                    <section>
                        <label>password</label>
                        <input
                            required
                            type='text'
                            name="password"
                            className={"password"}
                            value={updatetoken.password}
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
                            className={"token-number"}
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
