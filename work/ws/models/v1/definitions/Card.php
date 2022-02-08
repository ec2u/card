<?php

namespace app\models\definitions;

/**
 * @SWG\Definition(required={"europeanStudentCardNumber", "expiryDate", "europeanStudentIdentifier", "name", "emailAddress", "academicLevel", "picInstitutionCode"})
 *
 * @SWG\Property(property="europeanStudentCardNumber", type="string", description="The European Student Card Number which identifies the EC2U card")
 * @SWG\Property(property="cardType", type="integer", description="Type of cards: 1 - passive card, with no electronic, 2 - Smartcard without European common data zone, 3 - Smartcard with European common data zone, 4 - Smartcard on which application may be installed by service providers")
 * @SWG\Property(property="expiryDate", type="date", description="expiry date. ISO 8601 Date Time Format : yyyy-MM-ddTHH:mm:ss.SSSX")
 * @SWG\Property(property="name", type="string", description="Student's name")
 * @SWG\Property(property="emailAddress", type="string", description="Student's email address")
 * @SWG\Property(property="phoneNumber", type="string", description="Student's phone number")
 * @SWG\Property(property="academicLevel", type="integer", description="Current academic level of the student: 6 - bachelor’s degree, 7 - master's degree, 8 - doctorate")
 * @SWG\Property(property="picInstitutionCode", type="string", description="Participant Identification Code (PIC) code of the HEI that issued the card.")
 */
class Card
{
    public $europeanStudentCardNumber;      // ES. e6480dc0-9fba-1035-a6bd-001932465463
    public $cardType;
    public $expiryDate;
    public $europeanStudentIdentifier;      // ISO 8601 Date Time Format : yyyy-MM-ddTHH:mm:ss.SSSX
    public $name;                           // student name
    public $emailAddress;
    public $phoneNumber;
    public $academicLevel;
    public $picInstitutionCode;
}