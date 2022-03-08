import React, { useEffect } from "react";

export function Adduser() {
  const userdata = {
    admin: false,
    forename: "Tony",
    surname: "Stark",
    email: "Tony@argv.com",
  };

  useEffect(() => {
    fetch("/users/", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userdata),
    })
      .then((response) => response.json())
      .then((data) => console.log(data));
  }, []);

  return (
    <div>
      <form>
        <label>forename</label>
        <input type="text" required />
        <label>surname</label>
        <input type="text" required />
        <label>email</label>
        <input type="email" required />
        <label>admin</label>
        <input type="checkbox" required />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}
