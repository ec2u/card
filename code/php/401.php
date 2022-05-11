<?php
$config = include_once('../config.php');
include('../classes/LanguageManager.php');
$i18n = new LanguageManager($config);

include('../include/header.php');
// var_dump ($i18n)  
?>

<div class="container my-4">
  <!-- <?php var_dump ($i18n) ?> -->

    <h1 class="mt-0"> <?= $i18n->getText('016') ?></h1>
    <p><?= $i18n->getText('017') ?></p> 
    <br />    <br />    <br />

</div>

<?php
include('../include/footer.php');
?>

