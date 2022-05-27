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

                $code = $student->cards[$i]->europeanStudentCardNumber;

                $cards[$i] = array(

                    'code' => $code,
                    "test" => $esc["tst"] . $code, // !!! test for trailing slash in tst
                    "expiry" => substr($student->expiryDate, 0, 10),

                    "esi" => $student->europeanStudentIdentifier,
                    "level" => $student->academicLevel,
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

