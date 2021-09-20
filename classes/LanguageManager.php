<?php

class LanguageManager {
	
	public $config;

	function __construct($config)
	{
		$this->config = $config;
	}

	function getText($key)
	{
		$language = "en";
		if (isset($_GET['lang']) && $_GET['lang'] != "") 
			$language = $_GET['lang'];
		$text = include($this->config['homeDir'] . "/lang/" . $language . ".php");
		return $text[$key];
	}
}
