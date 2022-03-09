import { Trash } from "lucide-react";
import React from "react";
import "./dialog.css";

export function Dialog(props: any) {
  return (
    <div className="header">
      <div>
        <div className={"data"}>
          <Trash size={78} className="trash" />

          <span className="data-header">are you sure ?</span>
        </div>
        <div className="button">
          <div>
            <button className="yes" onClick={props.handleyes}>
              Yes
            </button>
          </div>
          <div>
            <button className="no" onClick={props.handleno}>
              No
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
