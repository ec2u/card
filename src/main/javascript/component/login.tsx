import React, { createElement } from 'react';
import { Link } from 'react-router-dom';
import logo from "../index.svg";
import './login.css';

export const LogIn = () => {
    return createElement("card-login", {},
        <>
            <header>
                <img
                    src={logo}
                />
            </header>

            <form>
                <span> Login </span>
                <label>Email Address</label>
                <input
                    type="email"
                />
                <label>Password</label>
                <input
                    type="password"
                />
                <Link to="/" >
                    <button type="submit" className={"button"} > Login</button>
                </Link>

            </form>


        </>

    );
}

