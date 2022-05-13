<?php
/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

function cards($esi, $tenant)
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

    $response = curl_exec($curl); // !!! catch network errors and report as 502
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

    curl_close($curl);

    try {

        if ($response !== false) {

            $student = json_decode($response);

            $cards = array();

            for ($i = 0; $i < count($student->cards); $i++) {
                $cards[$i] = array(

                    'code' => $student->cards[$i]->europeanStudentCardNumber,
                    "test" => $esc["tst"],
                    "expiry" => substr($student->expiryDate, 0, 10),

                    "esi" => $student->europeanStudentIdentifier,
                    "level" => $student->emailAddress,
                    "name" => $student->name,
                    "photo" => null, // !!! TBD

                    "hei" => $hei

                );
            }

            return $cards;

        } else if ($status == 404) {

            return array(); // !!! differentiate between unknown base API URL and unknown ESI

        } else {

            $error = json_decode($response);

            error_log("ESC error: " . $status . " " . property_exists($error, "error")
                ? $error->error . " / " . $error->error_description
                : "unknown error"
            );

            return $status / 100 == 4 ? 500 : 502;

        }

    } catch (Exception $e) {

        error_log("ESC exception: " . $e->getCode() . " " . $e->getMessage());

        return 500;

    }

}

?>

