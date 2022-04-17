import { Check, X } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./addtoken.css";


interface Token {
    readonly label: string;
    readonly serviceOrUserName: string;
    readonly serviceOrUserPassword: string;
    readonly id: number | string;
    readonly tokenNumber: number;

}


export function AddToken() {
    const [addtoken, setAddtoken] = useState({} as Token);
    const [disable, setDisable] = useState<Boolean>(false)
    const [loading, setLoading] = useState<Boolean>(false)
    const navigate = useNavigate();

    const tokenData = {
        serviceOrUserName: addtoken.serviceOrUserName,
        serviceOrUserPassword: addtoken.serviceOrUserPassword,
        tokenNumber: addtoken.tokenNumber,
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        const value = e.target.value

        if (!addtoken.serviceOrUserName) { setDisable(true) }


        else {
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
                    <a href="/tokens/" className={"tokens-link"}>Tokens </a>
                    <a>&#8250;</a>
                    <a> New Token</a>
                </section>

                <section>
                    <a >
                        {loading ? (<div className={"spinner"}></div>
                        ) : (
                            <Check onClick={handleSubmit}
                                size={38}
                                color={disable ? 'lightgray' : 'black'}
                                className={"button-check"}
                            />)}
                    </a>
                    <a href="/tokens/">
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
                            name="serviceOrUserName"
                            value={addtoken.serviceOrUserName}
                            onChange={handleChange}
                        />
                    </section>

                    <section>
                        <label>password</label>
                        <input
                            type="text"
                            required
                            name="serviceOrUserPassword"
                            value={addtoken.serviceOrUserPassword}
                            onChange={handleChange}
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
                        />
                    </section>

                </div>

            </form>
        </>
    );
}
