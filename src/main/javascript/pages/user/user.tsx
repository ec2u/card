import React, { createElement, useState } from "react";
import { Edit, LogOut } from 'lucide-react'
import './user.css'
import { Link } from "react-router-dom";

export function CardUser() {



    return createElement("card-addcard", {},
        <>
            <header>
                <span>Akhil Reddy Rondla</span>
                <Link to='/login/' title="Log Out">
                    <LogOut
                        size={40}
                        className={"logout-button"}
                    />
                </Link>

            </header>
            <section>
                <div className="fields">
                    <label>Forename</label>
                    <span> Akhil Reddy</span>

                    <label>Surname</label>
                    <span> Rondla</span>

                    <label>Email</label>
                    <span>akhilreddy.rondla01@universitadipavia.it</span>

                    <label>University</label>
                    <span> University Of Pavia</span>

                    <label>Department</label>
                    <span> Eletrical, Computer and Biomedical Engineering</span>

                    <label> Level Of Study</label>
                    <span>
                        Masters
                        {/* <select>
                        <option></option>
                        <option>Bachelors</option>
                        <option>Masters</option>
                        <option>Doctorate</option>
                    </select> */}
                    </span>
                </div>
                <div className="edit-icon" title="edit details">
                    <Edit
                        size={34}
                    />
                </div>




            </section>
        </>
    )
}