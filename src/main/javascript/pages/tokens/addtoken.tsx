import { Check, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./addtoken.css";


interface Token {
    readonly label: string;
    readonly username: string;
    readonly password: string;
    readonly id: number | string;
    readonly tokenNumber: any;

}


export function AddToken() {
    const [addtoken, setAddtoken] = useState({
        username: "",
        password: "",
        id: "",
        tokenNumber: 0,
    } as Token);
    const [disable, setDisable] = useState<Boolean>(true)
    const [loading, setLoading] = useState<Boolean>(false)
    const [numberError, setNumberError] = useState<string>("");
    const navigate = useNavigate();

    const tokenData = {
        username: addtoken.username,
        password: addtoken.password,
        tokenNumber: addtoken.tokenNumber,
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


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        const value = e.target.value

        if (addtoken.username === "" ||
            addtoken.password === "" ||
            addtoken.tokenNumber === 0 ||
            ((e.target.name = "tokenNumber") && !validateNumber(addtoken.tokenNumber)) ||
            value === "") {

            setDisable(true)
        } else {
            setDisable(false)
        }

        setAddtoken((addtoken) => ({
            ...addtoken,
            [e.target.name]: value,
        }));
    };

    const handleSubmit = async () => {
        if (disable) {

        } else {

            setLoading(true)
            await fetch("/tokens/", {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(tokenData),
            })
                .then(() => {
                    setLoading(false)
                    navigate('/tokens/');
                })
                .catch(error => console.warn('error:', error))
        }
    }


    return createElement("card-addtoken", {},
        <>
            <header>
                <section>
                    <a href="/tokens/" className={"tokens-link"} title="Tokens">Tokens &#8250;</a>
                    <a> New Token</a>
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
                    <a href="/tokens/" title="Close">
                        < X size={40} className={'button-close'} />
                    </a>
                </section>

            </header>

            <form>

                <div className={"start"}>

                    <section>
                        <label>username</label>
                        <input
                            type="text"
                            required
                            name="username"
                            value={addtoken.username}
                            onChange={handleChange}
                            className={"username"}
                        />
                    </section>

                    <section>
                        <label>password</label>
                        <input
                            type="text"
                            required
                            name="password"
                            value={addtoken.password}
                            onChange={handleChange}
                            className={"password"}
                        />
                    </section>



                </div>

                <div className={"end"}>

                    <section>
                        <label>token number</label>
                        <input
                            required
                            className="number"
                            type="text"
                            name="tokenNumber"
                            value={addtoken.tokenNumber}
                            onChange={handleChange}
                            pattern="[0-9]*"
                        />
                        <span>{numberError}</span>
                    </section>

                </div>

            </form>
        </>
    );
}
