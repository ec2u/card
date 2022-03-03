import React, { useEffect, useState } from "react";
import axios from "axios";
import "./style.css";
import { AiOutlinePlus } from "react-icons/ai";
import { Link } from "react-router-dom";
import { FaGreaterThan } from "react-icons/fa";
import { FiSearch } from "react-icons/fi";

interface responseData {
  forename: string;
  surname: string;
  email: string;
  admin: boolean;
}

const Users: React.FC = () => {
  const [response, setResponse] = useState<Array<responseData>>([]);

  useEffect(() => {
    axios
      .get(`https://621282f5f43692c9c6ecd419.mockapi.io/users`)
      .then((getData) => {
        setResponse(getData.data);
      });
  }, []);

  /*axios
    .post("http://localhost:8080/users/123", {
      forename: "Akhil",
      surname: "Rondla",
      email: "akhil@com",
      admin: true,
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });*/

  return (
    <div>
      <div className="topbar">
        <span>Users </span>
        <Link to="/users/add/" className="butons-add">
          <AiOutlinePlus />
        </Link>
      </div>

      <div className="header">
        <div className="header-section">
          <span>forename</span>
          <span>surname</span>
          <span>email</span>
        </div>
        <div className="header-search">
          <Link to="/users/search" className="butons">
            <FiSearch />
          </Link>
        </div>
      </div>
      <div className="divider"></div>
      {response.map((data, i) => {
        return (
          <div className="data" key={i}>
            <div className="data-section">
              <span>{data.forename} </span>
              <span>{data.surname}</span>
              <span>{data.email}</span>
            </div>
            <div className="data-inspect">
              <Link to="/users/inspect" className="butons-inspect">
                <FaGreaterThan />
              </Link>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default Users;
