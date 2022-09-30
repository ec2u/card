import React, { createElement, useState } from "react";
import { LogOut } from 'lucide-react'
import './user.css'
import { Link } from "react-router-dom";

export function CardUser() {



    return createElement("card-addcard", {},
        <>
            <header>
                <span>Akhil Reddy Rondla</span>
                <Link to='/login/'>
                    <LogOut
                        size={38}
                        className={"logout-button"}
                    /></Link>

            </header>
            <section>
                <label></label>
                <span></span>

            </section>
        </>
    )
}