<?php
/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

require_once('esc.php');
$tenants = include_once('tenants.php');


if (!isset($_SERVER['REMOTE_USER']) || $_SERVER['REMOTE_USER'] == "") {

    header("HTTP/1.1 401 Unauthorized");

} else {

    $user = user();

    if (is_string($user)) {

        header("HTTP/1.1 409 OK");
        header('Content-Type: application/json');

        echo json_encode($user);

    } else {

        $tenant = $tenants[$user["schac"]];

        if (!isset($tenant)) {

            header("HTTP/1.1 409 OK");
            header('Content-Type: application/json');

            echo json_encode("tenant-undefined");

        } else {

            $cards = cards($user["esi"], $tenant);

            if (!is_int($cards)) {

                header("HTTP/1.1 200 OK");
                header('Content-Type: application/json');

                echo json_encode(array(
                    'user' => $user,
                    'cards' => $cards
                ));

            } else if ($cards == 500) {

                header("HTTP/1.1 500 Internal Server Error");

            } else if ($cards == 502) {

                header("HTTP/1.1 502 Bad Gateway");

            }

        }
    }

}


function user()
{

    $user = array();

//    if (!isset($_SERVER["schacPersonalUniqueCode"]) || $_SERVER["schacPersonalUniqueCode"] == "") {
//        return "edugain-undefined-schacPersonalUniqueCode";
//    }
//
//     $user["esi"] = $_SERVER["schacPersonalUniqueCode"];

    $user["esi"] = "urn:schac:personalUniqueCode:int:esi:unipv.it:999001"; // TBD: to be removed

    if (!isset($_SERVER["cn"]) || $_SERVER["cn"] == "") {
        return "edugain-undefined-cn";
    }

    $user["name"] = $_SERVER["cn"];

    if (!isset($_SERVER["mail"]) || $_SERVER["mail"] == "") {
        return "edugain-undefined-mail";
    }

    $user["email"] = $_SERVER["mail"];

    if (!isset($_SERVER["schacHomeOrganisation"]) || $_SERVER["schacHomeOrganisation"] == "") {
        return "edugain-undefined-schacHomeOrganisation";
    }

    $user["schac"] = $_SERVER["schacHomeOrganisation"];

    return $user;

}


?>
