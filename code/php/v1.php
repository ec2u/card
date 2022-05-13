<?php
/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

require_once('esc.php');
$tenants = include_once('tenants.php');

if (!isset($_SERVER['REMOTE_USER']) || $_SERVER['REMOTE_USER'] == "") {
    header("HTTP/1.1 401 Unauthorized");
} else { 
    header("HTTP/1.1 200 OK");
    header('Content-Type: application/json');

    $user = array();

    if (!isset($_SERVER["schacPersonalUniqueCode"]) || $_SERVER["schacPersonalUniqueCode"] == "") {
	// TBD: to be decommented
	//echo json_encode(array(
	//	'errorCode' => '001',
	//	'description' => 'Esi not available in EduGain'
	//));
	// return;
    }
    // $user["esi"] = $_SERVER["schacPersonalUniqueCode"]; // TBD: to be decommented
    $user["esi"] = "urn:schac:personalUniqueCode:int:esi:unipv.it:999001"; // TBD: to be removed

    if (!isset($_SERVER["cn"]) || $_SERVER["cn"] == "") {
	echo json_encode(array(
		'errorCode' => '002',
		'description' => 'Common Name not available in EduGain'
	));
	return;
    }
    $user["name"] = $_SERVER["cn"];;

    if (!isset($_SERVER["mail"]) || $_SERVER["mail"] == "") {
	echo json_encode(array(
		'errorCode' => '003',
		'description' => 'Email Address not available in EduGain'
	));
	return;
    }
    $user["email"] = $_SERVER["mail"];

    if (!isset($_SERVER["schacHomeOrganisation"]) || $_SERVER["schacHomeOrganisation"] == "") {
	echo json_encode(array(
		'errorCode' => '004',
		'description' => 'Schac Home Organisation not available in EduGain'
	));
	return;
    }
    $user["schac"] = $_SERVER["schacHomeOrganisation"];

    if (!isset($tenants[$user["schac"]])) {
    	echo json_encode(array(
		'errorCode' => '005',
		'description' => 'Tenant not available in configuration'
	));
	return;
    }

    $tenant = $tenants[$user["schac"]];

    $cards = cards($user["esi"], $tenant); // !!! exception handling

    $profile = json_encode(array(
        'user' => $user,
        'cards' => $cards
    ));

    echo $profile;

}

?>
