<?php
/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

function cards($esi, $tenant) // !!! exception handling
{

    $hei = $tenant["hei"];
    $esc = $tenant["esc"];

    $curl = curl_init();

    curl_setopt($curl, CURLOPT_URL,
        $esc["api"] . "/students/" . $esi
    );

    curl_setopt($curl, CURLOPT_HTTPHEADER, array(
        'Key: ' . $esc["key"]
    ));

    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

    $result = curl_exec($curl);

    if ($result === false)
    {
	    return array('errorCode' => '006',
		    	 'description' => curl_error($curl)
		 );
    }

    curl_close($curl);

    $student = json_decode($result);

    if (is_null($student) || !property_exists($student, 'europeanStudentIdentifier'))
    {
	    return array('errorCode' => '007',
		         'description' => property_exists($student->error_description) ? $student->error_description : "Unknown Error during ESC interaction"
	    );
    }

    $cards = array();

    for ($i = 0; $i < count($student->cards); $i++)
    {
	    $cards[$i] = array(

	        'code' => $student->cards[$i]->europeanStudentCardNumber,
	        "test" => $esc["tst"],
        	"expiry" => $student->expiryDate,
	        "esi" => $student->europeanStudentIdentifier,
	        "level" => $student->emailAddress,
	        "name" => $student->name,
	        "photo" => null,        // TBD

	        "hei" => $hei

    	);
    }

    return $cards; 


}

?>

