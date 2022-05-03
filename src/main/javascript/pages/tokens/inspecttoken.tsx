import { Edit } from "lucide-react";
import React, { createElement, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./inspecttoken.css";

interface Token {
    readonly label: string;
    readonly username: string;
    readonly password: string;
    readonly id: number | string;
    readonly tokenNumber: number;
}



export function TokenInspect() {
    const [token, setToken] = useState<Token>({} as Token);
    const [loading, setLoading] = useState<Boolean>(false)
    const { id } = useParams();


    useEffect(() => {
        setLoading(true)
        const fetchData = async () => {
            await fetch(`/tokens/${id}`, {
                headers: {
                    Accept: "application/json",
                },
            })
                .then((response) => response.json())
                .then((data) => setToken(data));
            setLoading(false)
        }
        fetchData();
    }, []);


    return createElement("card-inspecttoken", {},
        <>
            <header>

                <section>
                    <a href='/tokens/' className={"tokens-link"} title="Tokens">Tokens &#8250;</a>
                    <a>{token.label}</a>
                </section>

                <a href={`/edit${token.id}`} title="Edit">
                    <Edit size={38} className="button-edit" />
                </a>



            </header>
            {loading ? (
                <div className={"spinner"}></div>
            ) :
                (<form>

                    <div className={"start"}>

                        <section>
                            <label>username</label>
                            <span>{token.username}</span>
                        </section >

                        <section>
                            <label>password</label>
                            <span>{token.password} </span>
                        </section>


                    </div >

                    <div className={"end"}>
                        <section>
                            <label> token number</label>
                            <span>{token.tokenNumber}</span>
                        </section>
                    </div>

                </form >)}


        </>
    );
}
