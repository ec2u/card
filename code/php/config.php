<?php
	return ['routerUrl' 	=> 'https://api.europeanstudentcard.eu/v1',
		'apiKey'	=> 'IDIFsfHMTj9Jj6Ce8d6A6iBLdOkYzqAm',
		'testUrl'	=> "http://pp.esc.gg/fc3a1f80-93fb-103a-9b31-000999893752",
		'whitelist'	=> [
					'https://card.ec2u.eu',
					'https://card.ec2u.dev',
					'https://127.0.0.1:3000',
					'http://localhost:3000',
				],
		'page401'	=> 'https://card.ec2u.eu/pages/401.php',
		'999893752'	=> [
					'name' => 'University of Pavia',
					'iso' => 'IT',
					'country' => 'Italy'
				],
	];
