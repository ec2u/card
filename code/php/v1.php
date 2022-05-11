<?php
/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

include('esc.php');

$tenants = include_once('tenants.php');


if (!isset($_SERVER['REMOTE_USER']) || $_SERVER['REMOTE_USER'] == "") {

    header("HTTP/1.1 401 Unauthorized");

} else {

    header("HTTP/1.1 200 OK");
    header('Content-Type: application/json');

    $user = array(
        'esi' => $_SERVER["schacPersonalUniqueCode"],
        'name' => $_SERVER['cn'],
        'email' => $_SERVER['mail'],
        'schac' => $_SERVER["schacHomeOrganisation"]
    );

    $esi = $user["esi"];
    $schac = $user["schac"];

    $tenant = $tenants[$schac];


    if (!isset($esi)) {

        // !!! $esi = "urn:schac:personalUniqueCode:int:esi:unipv.it:999001"; // !!!

    } else if (!isset($schac)) {

        // !!!

    } else {


    }


    $cards = cards($esi, $tenant); // !!! exception handling

    $profile = json_encode(array(
        'user' => $user,
        'cards' => $cards
    ));

    echo $profile;

}

?>
