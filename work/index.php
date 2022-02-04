<?php
$config = include_once 'config.php';
include 'classes/LanguageManager.php';
$i18n = new LanguageManager($config);
include 'include/header.php';
?>

<div class="container my-4">
<!--  <h1>Lorem Ipsum</h1>
  <p>Ab illo tempore, ab est sed immemorabili.<br/>
    Ullamco laboris nisi ut aliquid ex ea commodi consequat.<br/>
    Quis aute iure reprehenderit in voluptate velit esse.<br/>
    Petierunt uti sibi concilium totius Galliae in diem certam indicere.</p>
  <p>Pellentesque habitant morbi tristique senectus et netus.</p>
  <h2>Esempio di Card</h2>
  <div class="my-5">
    <div class="container">
      <div class="row">
        <div class="col-12 col-md-6 col-lg-4">
          <div class="card-wrapper card-space">
            <div class="card card-bg card-big">
              <div class="card-body">
                <div class="top-icon">
                  <svg class="icon">
                    <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-card"></use>
                  </svg>
                </div>
                <h5 class="card-title">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor…</h5>
                <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                <a class="read-more" href="#">
                  <span class="text">Leggi di più</span>
                  <svg class="icon">
                    <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-arrow-right"></use>
                  </svg>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  -->
  <!--<h2>Esempio di Form</h2>
  <div class="my-5">
    <div class="form-row">
      <div class="form-group col-md-6">
        <input type="email" class="form-control" id="inputEmail4" placeholder="inserisci il tuo indirizzo email">
        <label for="inputEmail4">Email</label>
      </div>
      <div class="form-group col-md-6">
        <input type="password" class="form-control input-password" id="exampleInputPassword" placeholder="inserisci la tua password">
        <span class="password-icon" aria-hidden="true">
        <svg class="password-icon-visible icon icon-sm"><use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-password-visible"></use></svg>
        <svg class="password-icon-invisible icon icon-sm d-none"><use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-password-invisible"></use></svg>
      </span>
        <label for="exampleInputPassword">Password</label>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col">
        <input type="text" class="form-control" id="inputAddress" placeholder="Via Roma, 1">
        <label for="inputAddress">Indirizzo</label>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <input type="text" class="form-control" id="inputCity">
        <label for="inputCity">Comune</label>
      </div>
      <div class="form-group col-md-2">
        <input type="text" class="form-control" id="inputCAP">
        <label for="inputCAP">CAP</label>
      </div>
      <div class="col-md-4">
        <div class="bootstrap-select-wrapper">
          <label>Provincia</label>
          <select title="Scegli un'opzione">
            <option value="Value 1">Opzione 1</option>
            <option value="Value 2">Opzione 2</option>
            <option value="Value 3">Opzione 3</option>
            <option value="Value 4">Opzione 4</option>
            <option value="Value 5">Opzione 5</option>
          </select>
        </div>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <div class="toggles">
          <label for="toggleEsempio1a">
            Label dell'interruttore 1
            <input type="checkbox" id="toggleEsempio1a">
            <span class="lever"></span>
          </label>
        </div>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col text-center">
        <button type="button" class="btn btn-outline-primary">Annulla</button>
        <button type="submit" class="btn btn-primary">Conferma</button>
      </div>
    </div>
  </div> -->


    <h1 class="mt-0">   <img class="align-self-start mr-3" src="<?= $config['homePage'] ?>/img/id-card.png" width=8%><?= $i18n->getText('000') ?> </h1>
    <p><?= $i18n->getText('001') ?></p>


</div>

<?php
include('include/footer.php');
?>

