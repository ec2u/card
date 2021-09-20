<!doctype html>
<html lang="en">
<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Bootstrap Italia Playground">
  <meta name="author" content="">
  <meta name="generator" content="">
  <meta name="robots" content="noindex">

  <title>EC2U Card</title>

  <!-- Bootstrap Italia CSS -->
  <!-- <link href="/node_modules/bootstrap-italia/dist/css/bootstrap-italia.min.css" rel="stylesheet"> -->

  <!-- Bootstrap Italia custom CSS -->
  <link href="<?= $config['homePage'] ?>/css/compiled/bootstrap-italia-custom.min.css" rel="stylesheet">

  <!-- App styles -->
  <link href="<?= $config['homePage'] ?>/css/main.css" rel="stylesheet">

  <!-- Favicons -->
  <link rel="apple-touch-icon" href="<?= $config['homePage'] ?>/img/favicons/apple-touch-icon.png">
  <link rel="icon" href="<?= $config['homePage'] ?>/img/favicons/favicon-32x32.png" sizes="32x32" type="image/png">
  <link rel="icon" href="<?= $config['homePage'] ?>/img/favicons/favicon-16x16.png>" sizes="16x16" type="image/png">
  <link rel="manifest" href="<?= $config['homePage'] ?>/img/favicons/manifest.webmanifest">
  <link rel="mask-icon" href="<?= $config['homePage'] ?>/img/favicons/safari-pinned-tab.svg" color="#0066CC">
  <link rel="icon" href="<?= $config['homePage'] ?>/img/favicons/favicon.ico">
  <meta name="msapplication-config" content="<?= $config['homeDir'] ?>/img/favicons/browserconfig.xml">
  <meta name="theme-color" content="#0066CC">

</head>

<body>

<div class="it-header-wrapper">
  <div class="it-header-slim-wrapper">
    <div class="container">
      <div class="row">
        <div class="col-12">
          <div class="it-header-slim-wrapper-content">
            <a class="d-none d-lg-block navbar-brand" href="#">EC2U - European Campus of City-Universities</a>
            <div class="nav-mobile">
              <nav>
                <a class="it-opener d-lg-none" data-toggle="collapse" href="#menu-principale" role="button" aria-expanded="false" aria-controls="menu-principale">
                  <span>EC2U - European Campus of City-Universities</span>
                  <svg class="icon">
                    <use xlink:href="<?= $config['homePage'] ?>/node_modules/bootstrap-italia/dist/svg/sprite.svg#it-expand"></use>
                  </svg>
                </a>
                <div class="link-list-wrapper collapse" id="menu-principale">
                  <ul class="link-list">
                    <li><a href="<?= $config['homePage'] ?>/index.php?lang=<?= $_GET['lang'] ?>">Home</a></li>
		    <li><a href="<?= $config['homePage'] ?>/pages/information.php?lang=<?= $_GET['lang'] ?>"><?= $i18n->getText("005") ?></a></li>
                    <li><a href="<?= $config['homePage'] ?>/pages/privacy.php?lang=<?= $_GET['lang'] ?>">Privacy</a></li>
                    <!-- <li><a href="#">Link 2 Active</a></li> -->
                  </ul>
                </div>
              </nav>
            </div>
            <div class="header-slim-right-zone">
              <div class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-expanded="false">
                  <span><?php if (isset($_GET['lang']) && $_GET['lang'] != "") echo strtoupper($_GET['lang']); else echo 'EN'; ?></span>
                  <svg class="icon d-none d-lg-block">
                    <use xlink:href="<?= $config['homePage'] ?>/node_modules/bootstrap-italia/dist/svg/sprite.svg#it-expand"></use>
                  </svg>
                </a>
                <div class="dropdown-menu">
                  <div class="row">
                    <div class="col-12">
                      <div class="link-list-wrapper">
                        <ul class="link-list">
                          <li><a class="list-item" href="?lang=en"><span>EN</span></a></li>
                          <li><a class="list-item" href="?lang=it"><span>IT</span></a></li>
                          <!-- <li><a class="list-item" href="#"><span>ITA</span></a></li> -->
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="it-access-top-wrapper">
                <!-- <button class="btn btn-primary btn-sm" href="eCard/eCard.php" type="button">Open your eCard</button> -->
                <a class="btn btn-primary btn-sm" href="<?= $config['homePage'] ?>/eCard/eCard.php?lang=<?= $_GET['lang'] ?>"><?= $i18n->getText("004") ?></a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="it-nav-wrapper">
    <div class="it-header-center-wrapper">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <div class="it-header-center-content-wrapper">
              <div class="it-brand-wrapper">
                <a href="#">
                  <!--<svg class="icon">
                    <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-code-circle"></use>
                  </svg> -->
                  <img src="<?= $config['homePage'] ?>/img/ec2uLogo.png" alt="image Alt" title="Image Title" width="10%">
                  <div class="it-brand-text ml-2">
                    <h2 class="no_toc">EC2U Card</h2>
                    <h3 class="no_toc d-none d-md-block">European Campus of City-Universities</h3>
                  </div>
                </a>
              </div>
              <!-- <div class="it-right-zone">
                <div class="it-socials d-none d-md-flex">
                  <span>Seguici su</span>
                  <ul>
                    <li>
                      <a href="#" aria-label="Facebook" target="_blank">
                        <svg class="icon">
                          <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-facebook"></use>
                        </svg>
                      </a>
                    </li>
                    <li>
                      <a href="#" aria-label="Github" target="_blank">
                        <svg class="icon">
                          <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-github"></use>
                        </svg>
                      </a>
                    </li>
                    <li>
                      <a href="#" target="_blank" aria-label="Twitter">
                        <svg class="icon">
                          <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-twitter"></use>
                        </svg>
                      </a>
                    </li>
                  </ul>
                </div>
                <div class="it-search-wrapper">
                  <span class="d-none d-md-block">Cerca</span>
                  <a class="search-link rounded-icon" href="#" aria-label="Cerca">
                    <svg class="icon">
                      <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-search"></use>
                    </svg>
                  </a>
                </div> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="it-header-navbar-wrapper">
      <div class="container">
        <div class="row">
          <div class="col-12">

            <!-- <nav class="navbar navbar-expand-lg has-megamenu">
              <button class="custom-navbar-toggler" type="button" aria-controls="nav10" aria-expanded="false" aria-label="Toggle navigation" data-target="#nav10">
                <svg class="icon">
                  <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-burger"></use>
                </svg>
              </button>
              <div class="navbar-collapsable" id="nav10">
                <div class="overlay"></div>
                <div class="close-div sr-only">
                  <button class="btn close-menu" type="button"><span class="it-close"></span>close</button>
                </div>
                <div class="menu-wrapper">
                  <ul class="navbar-nav">
                    <li class="nav-item active"><a class="nav-link active" href="#"><span>link 1 attivo</span><span class="sr-only">current</span></a></li>
                    <li class="nav-item"><a class="nav-link" href="#"><span>link 2</span></a></li>
                    <li class="nav-item"><a class="nav-link disabled" href="#"><span>link 3 disabilitato</span></a></li>
                    <li class="nav-item dropdown">
                      <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-expanded="false">
                        <span>Esempio di Dropdown</span>
                        <svg class="icon icon-xs">
                          <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-expand"></use>
                        </svg>
                      </a>
                      <div class="dropdown-menu">
                        <div class="link-list-wrapper">
                          <ul class="link-list">
                            <li>
                              <h3 class="no_toc" id="heading">Heading</h3>
                            </li>
                            <li><a class="list-item" href="#"><span>Link list 1</span></a></li>
                            <li><a class="list-item" href="#"><span>Link list 2</span></a></li>
                            <li><a class="list-item" href="#"><span>Link list 3</span></a></li>
                            <li><span class="divider"></span></li>
                            <li><a class="list-item" href="#"><span>Link list 4</span></a></li>
                          </ul>
                        </div>
                      </div>
                    </li>
                    <li class="nav-item dropdown megamenu">
                      <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-expanded="false">
                        <span>Esempio di Megamenu</span>
                        <svg class="icon icon-xs">
                          <use xlink:href="node_modules/bootstrap-italia/dist/svg/sprite.svg#it-expand"></use>
                        </svg>
                      </a>
                      <div class="dropdown-menu">
                        <div class="row">
                          <div class="col-12 col-lg-4">
                            <div class="link-list-wrapper">
                              <ul class="link-list">
                                <li>
                                  <h3 class="no_toc">Heading 1</h3>
                                </li>
                                <li><a class="list-item" href="#"><span>Link list 1 </span></a></li>
                                <li><a class="list-item" href="#"><span>Link list 2 </span></a></li>
                                <li><a class="list-item" href="#"><span>Link list 3 </span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="col-12 col-lg-4">
                            <div class="link-list-wrapper">
                              <ul class="link-list">
                                <li>
                                  <h3 class="no_toc">Heading 2</h3>
                                </li>
                                <li><a class="list-item" href="#"><span>Link list 1 </span></a></li>
                                <li><a class="list-item" href="#"><span>Link list 2 </span></a></li>
                                <li><a class="list-item" href="#"><span>Link list 3 </span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="col-12 col-lg-4">
                            <div class="link-list-wrapper">
                              <ul class="link-list">
                                <li>
                                  <h3 class="no_toc">Heading 3</h3>
                                </li>
                                <li><a class="list-item" href="#"><span>Link list 1 </span></a></li>
                                <li><a class="list-item" href="#"><span>Link list 2 </span></a></li>
                                <li><a class="list-item" href="#"><span>Link list 3</span></a></li>
                              </ul>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>

              </div>
            </nav> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


  

