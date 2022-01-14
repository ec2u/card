<?php

// See: https://www.yiiframework.com/doc/guide/2.0/en/rest-quick-start
// See: https://www.yiiframework.com/wiki/748/building-a-rest-api-in-yii2-0
// See: https://github.com/yii2mod/yii2-swagger
// curl -k -i -H "Accept:application/json" "https://devlibra8.unipv.it/luigi/ec2uCard/ws/web/cards/50"

namespace app\controllers\v1;

use yii\rest\Controller;
use yii\filters\VerbFilter;
use yii;

class CardController extends Controller
{
    // URL: <version>/cards
    // method:GET
    /**
     * @SWG\Get(path="cards",
     *     tags={"Card"},
     *     summary="Retrieves the collection of all issued cards.",
     *     @SWG\Response(
     *         response = 200,
     *         description = "Card collection response",
     *         @SWG\Schema(ref = "#/definitions/Card")
     *     ),
     * )
     */
    public function actionIndex()
    {
        // ritorna la lista di tutte le card
        $this->setHeader(200);
        echo json_encode(array('value'=>"Full List"), JSON_PRETTY_PRINT);
    }



    // URL: <version>/cards/{cardId}
    // method:GET
    /**
     * @SWG\Get(path="cards/{cardId}",
     *     tags={"Card"},
     *     summary="Visualizes an issued card.",
     *     @SWG\Response(
     *         response = 200,
     *         description = "Visualizes an issued card",
     *         @SWG\Schema(ref = "#/definitions/Card")
     *     ),
     * )
     */
    public function actionView($id)
    {
        $this->setHeader("200");
        echo json_encode(array('value'=>"View $id"), JSON_PRETTY_PRINT);
    }


    // URL: <version>/cards
    // method: POST
    /**
     * @SWG\Post(path="cards",
     *     tags={"Card"},
     *     summary="Creates and issues a new card.",
     *     @SWG\Response(
     *         response = 200,
     *         description = "Creates and issues a new card",
     *         @SWG\Schema(ref = "#/definitions/Card")
     *     ),
     * )
     */
    public function actionCreate() {
        $params=$_REQUEST;
        $this->setHeader("200");
        echo json_encode(array('value'=>"Creato"), JSON_PRETTY_PRINT);
    }

    // URL: <version>/cards/{cardId}
    // method:POST
    /**
     * @SWG\Post(path="cards/{cardId}",
     *     tags={"Card"},
     *     summary="Updates an existing card.",
     *     @SWG\Response(
     *         response = 200,
     *         description = "Updates an existing card",
     *         @SWG\Schema(ref = "#/definitions/Card")
     *     ),
     * )
     */
    public function actionUpdate($id) {
        $params=$_REQUEST;
        $this->setHeader("200");
        echo json_encode(array('value'=>"update"), JSON_PRETTY_PRINT);
    }

    // URL: <version>/cards/{cardId}
    // method:DELETE
    /**
     * @SWG\Delete(path="cards/{cardId}",
     *     tags={"Card"},
     *     summary="Deletes an existing card.",
     *     @SWG\Response(
     *         response = 200,
     *         description = "Deletes an existing card",
     *         @SWG\Schema(ref = "#/definitions/Card")
     *     ),
     * )
     */
    public function actionDelete($id) {
        $params=$_REQUEST;
        $this->setHeader("200");
        echo json_encode(array('value'=>"deleted"), JSON_PRETTY_PRINT);
    }

    private function setHeader($status)
    {

        $status_header = 'HTTP/1.1 ' . $status . ' ' . $this->_getStatusCodeMessage($status);
        $content_type="application/json; charset=utf-8";

        header($status_header);
        header('Content-type: ' . $content_type);
        header('X-Powered-By: ' . "University of Pavia - Italy");
    }
    private function _getStatusCodeMessage($status)
    {
        // these could be stored in a .ini file and loaded
        // via parse_ini_file()... however, this will suffice
        // for an example
        $codes = Array(
            200 => 'OK',
            400 => 'Bad Request',
            401 => 'Unauthorized',
            402 => 'Payment Required',
            403 => 'Forbidden',
            404 => 'Not Found',
            500 => 'Internal Server Error',
            501 => 'Not Implemented',
        );
        return (isset($codes[$status])) ? $codes[$status] : '';
    }

    /*public function behaviors()
    {
        return [
            'verbs' => [
                'class' => VerbFilter::className(),
                'actions' => [
                    'index'=>['get'],
                    'view'=>['get'],
                    'create'=>['post'],
                    'update'=>['post'],
                    'delete' => ['delete'],
                ],
            ]
        ];
    }*/

}

?>
