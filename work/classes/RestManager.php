<?php

class RestManager {

	public $config;

	function __construct($config)
	{
		$this->config = $config;
	}

	/*function getText($key)
	{
		$language = "en";
		if (isset($_GET['lang']) && $_GET['lang'] != "") 
			$language = $_GET['lang'];
		$text = include($this->config['homeDir'] . "/lang/" . $language . ".php");
		return $text[$key];
	}*/

	function getStudent($studentIdentifier)
	{
    	    $curl = curl_init();

	    
	    curl_setopt($curl, CURLOPT_HTTPHEADER, array('key: '. $this->config['apiKey']));
	    	
            /*curl_setopt($curl, CURLOPT_HTTPHEADER, array(
                   'Content-Type: '.$contentType
            ));*/
	
	    curl_setopt($curl, CURLOPT_URL, $this->config['routerUrl'] . "/students/" . $studentIdentifier);
	    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
	
	    $result = curl_exec($curl);
	
	    curl_close($curl);
	
	    return json_decode($result);
	}

}
