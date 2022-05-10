import React, { createElement, useState } from "react";
import { LogOut } from 'lucide-react'
import './user.css'

export function CardUser() {



    return createElement("card-addcard", {},
        <>
            <header>
                <span>Akhil Reddy Rondla</span>
                <LogOut
                    size={38}

                    className={"logout-button"}
                />
            </header>
        </>
    )
}