import { Edit } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./inspecttoken.css";

interface Token {
    readonly label: string;
    readonly serviceOrUserName: string;
    readonly serviceOrUserPassword: string;
    readonly id: number | string;
    readonly tokenNumber: number;
}



export function TokenInspect() {
    const [token, setToken] = useState<Token>({} as Token);
    const [loading, setLoading] = useState<Boolean>(false)
    const { id } = useParams();


    useEffect(() => {
        setLoading(true)
        fetch(`/tokens/${id}`, {
            headers: {
                Accept: "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setToken(data));
        setLoading(false)
    }, []);


    return createElement("card-inspecttoken", {},
        <>
            <header>

                <section>
                    <a href='/tokens/' className={"tokens-link"}>Tokens &#8250;</a>
                    <a>{token.label}</a>
                </section>

                <a href={`/edit${token.id}`}>
                    <Edit size={38} className="button-edit" />
                </a>



            </header>

            <form>

                <div className={"start"}>

                    <section>
                        <label>username</label>
                        <span>{token.serviceOrUserName}</span>
                    </section>

                    <section>
                        <label>password</label>
                        <span>{token.serviceOrUserPassword} </span>
                    </section>


                </div>

                <div className={"end"}>
                    <section>
                        <label> token number</label>
                        <span>{token.tokenNumber}</span>
                    </section>
                </div>

            </form>

        </>
    );
}
