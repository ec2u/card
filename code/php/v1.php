<?php
/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

require_once('esc.php');

$conf = "/etc/ec2u-card/"; // !!! from setup module
$tenants = json_decode(file_get_contents($conf . "tenants.json"), JSON_OBJECT_AS_ARRAY); // !!! exception handling


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

    if (!isset($_SERVER["cn"]) || $_SERVER["cn"] == "") {
        return "edugain-undefined-cn";
    }

    $user["name"] = $_SERVER["cn"];

    if (!isset($_SERVER["mail"]) || $_SERVER["mail"] == "") {
        return "edugain-undefined-mail";
    }

    $user["email"] = $_SERVER["mail"];

    if (!isset($_SERVER["schacPersonalUniqueCode"]) || $_SERVER["schacPersonalUniqueCode"] == "") {
        return "edugain-undefined-schacPersonalUniqueCode";
    }

    $user["esi"] = $_SERVER["schacPersonalUniqueCode"];

    if (!isset($_SERVER["schacHomeOrganisation"]) || $_SERVER["schacHomeOrganisation"] == "") {
        return "edugain-undefined-schacHomeOrganisation";
    }

    $user["schac"] = $_SERVER["schacHomeOrganisation"];

    // !!! eduPersonScopedAffiliation

    return $user;

}


?>
