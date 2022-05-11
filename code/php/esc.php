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

    curl_close($curl);

    $student = json_decode($result);

    array(

        'code' => "!!!",
        "test" => $esc["tst"],
        "expiry" => $student['expiry'],

        "esi" => $student['esi'],
        "level" => $student['academicLevel'],
        "name" => $student['name'],
        "photo" => null,        // TBD

        "hei" => $hei

    )


    return $student; // !!! loop


}



