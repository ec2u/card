import { Trash, X } from "lucide-react";
import React from "react";
import "./dialog.css";

export function Dialog(props: any) {
  return (
    <div className="header">
      <div className="delete">
        <div className={"data"}>
          <Trash size={78} className="trash" />

          <span className="data-header">are you sure ?</span>

        </div>
        {/* <X className="close" size={43} color='red' /> */}
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
