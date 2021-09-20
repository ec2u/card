<?php
$config = include_once('../config.php');
include('../classes/LanguageManager.php');
$i18n = new LanguageManager($config);

include('../include/header.php');
?>

<div class="container">

    <!-- <h1 class="mt-0">   <img class="align-self-start mr-3" src="../img/id-card.png" width=8%> Your card</h1> -->

<br />
<!-- <div class="row">
    <div class="col"></div>
    <div class="rounded border" style="width: 360px; height:200px;background-color:#f4f4f4;">
	<div class="row" style="height:90%">	
		    <div class="col-4 align-middle" style="height:100%">
    		        <div class="row" style="height:20%"></div>
			<div class="row" style="height:40%"><img src="<?= $config['homePage'] ?>/img/profile.png" width=95% style="padding:10px;"></div>
    		        <div class="row" style="height:20%"></div>
		    </div>
		    <div class="col-6">
			<div class="row" style="height:3%"></div>
			<div class="row" style="font-size:xx-small;"><b>INSTITUTION</b></div>
			<div class="row" style="font-size:xx-small;margin-bottom:1px;"><i>University of Pavia</i></div>
			<div class="row" style="font-size:xx-small;"><b>NAME</b></div>
			<div class="row" style="font-size:xx-small;margin-bottom:1px;"><i>LUIGI SANTANGELO</i></div>
			<div class="row" style="font-size:xx-small;"><b>IDENTIFIER</b></div>
			<div class="row" style="font-size:xx-small;margin-bottom:1px;"><i>123456789163</i></div>
			<div class="row" style="font-size:xx-small;"><b>EMAIL</b></div>
			<div class="row" style="font-size:xx-small;margin-bottom:1px;"><i>luigi.santangelo@unipv.it</i></div>
			<div class="row" style="font-size:xx-small;"><b>LEVEL</b></div>
			<div class="row" style="font-size:xx-small;margin-bottom:1px;"><i>DOCTORATE</i></div>
			<div class="row" style="font-size:xx-small;"><b>EXPIRING DATE</b></div>
			<div class="row" style="font-size:xx-small;"><i>December 31, 2030</i></div>
    		        <div class="row" style="height:3%"></div>
		    </div>
		    <div class="col-2">
			<img src="<?= $config['homePage'] ?>/img/ec2uLogo.png" width=95% style="padding:5px;">
			<img src="<?= $config['homePage'] ?>/img/qr-code.png" width=95% style="padding:5px;">
		    </div>
	</div>
	<div class="row">	
		    <div class="col-sm" style="font-size:xx-small;margin:5px;"><i>Card number a1b2c3d4e5a1b2c3d4e5</i></div>
	</div>
    </div>
    <div class="col"></div>
  </div>
</div> -->
<br /> 

<div class="row">
	<div class="col p-2"></div>
	<div class="col rounded border p-2" style="width:360px;background-color:#f4f4f4">
		<div class="text-center">
			<!-- <img src="https://chart.googleapis.com/chart?chs=340x340&cht=qr&chl=http://www.google.com&choe=UTF-8" title="Link to Google.com" /> -->
			<img src="https://quickchart.io/qr?text=http://esc.gg/22d30120-cd32-1039-bbec-001999893752&light=f4f4f4&ecLevel=Q&margin=1&size=340&format=svg" title="" />
			<!-- see https://quickchart.io/qr-code-api/ -->
		</div>
		<!-- <div class="text-center m-2">
			<img src="<?= $config['homePage'] ?>/img/ec2uLogo.png" alt="image Alt" title="Image Title" width="20%">
		</div> -->
		<div class="text-center mb-3">
			<h1>European Campus of City-Universities</h1>
		</div>
		<div class="text-center">
			<img src="<?= $config['homePage'] ?>/img/profile.png" width=200px; style="padding:10px;">
		</div>
		<div class="row ml-2"><b>INSTITUTION</b></div>
		<div class="row ml-2 mb-3"><i>University of Pavia</i></div>
		<div class="row ml-2"><b>NAME</b></div>
		<div class="row ml-2 mb-3"><i>LUIGI SANTANGELO</i></div>
		<div class="row ml-2"><b>IDENTIFIER</b></div>
		<div class="row ml-2 mb-3"><i>123456789163</i></div>
		<div class="row ml-2"><b>EMAIL</b></div>
		<div class="row ml-2 mb-3"><i>luigi.santangelo@unipv.it</i></div>
		<div class="row ml-2"><b>LEVEL</b></div>
		<div class="row ml-2 mb-3"><i>DOCTORATE</i></div>
		<div class="row ml-2"><b>EXPIRING DATE</b></div>
		<div class="row ml-2 mb-3"><i>December 31, 2030</i></div>
	</div>
	<div class="col p-2"></div>
</div>

<br />

</div>




<?php
include('../include/footer.php');
?>
