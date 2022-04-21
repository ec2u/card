import { Trash2 } from "lucide-react";
import React, { createElement } from "react";
import "./deletedialog.css";

export function Deletedialog(props: any) {
  return createElement('card-deletebox', {}, <>
    <div className={"header"}>

      <div className={"data"}>
        <Trash2 size={80} className={"trash"} />

        <span className={"data-header"}>are you sure ?</span>

      </div>

      <div className={"button"}>
        <div>
          <button className={"yes"} onClick={props.handleyes}>
            Yes
          </button>
        </div>
        <div>
          <button className={"no"} onClick={props.handleno}>
            No
          </button>
        </div>
      </div>

    </div>
  </>

  );
}
