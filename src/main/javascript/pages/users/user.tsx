import { useState } from "react";


export const LinePattern="\\S+( \\S+)*";

export interface User {

    readonly admin: boolean;

    readonly forename: string;
    readonly surname: string;

    readonly email: string;

}

export function ToolUser() {

    const [user, setUser]=useState();

    return <form>

        <input type={"text"} value={user[0]} pattern={LinePattern}/>

    </form>;
}