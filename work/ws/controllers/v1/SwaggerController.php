<?php

namespace app\controllers\v1;

use Yii;
use yii\helpers\Url;
use yii\web\Controller;

/**
 * @SWG\Swagger(
 *     basePath="ws/web/v1/",
 *     produces={"application/json"},
 *     consumes={"application/x-www-form-urlencoded"},
 *     @SWG\Info(
 *         version="1.0.0",
 *         title="EC2U Card Management API",
 *         description="The following APIs allow to manage issueing EC2U Cards",
 *         termsOfService="",
 *         @SWG\Contact(
 *             email="luigi.santangelo@unipv.it"
 *         ),
 *     ),
 * )
 */
class SwaggerController extends Controller
{
    /**
     * @inheritdoc
     */
    public function actions(): array
    {
        return [
            'docs' => [
                'class' => 'yii2mod\swagger\SwaggerUIRenderer',
                'restUrl' => Url::to(['v1/json-schema']),
            ],
            'json-schema' => [
                'class' => 'yii2mod\swagger\OpenAPIRenderer',
                // Тhe list of directories that contains the swagger annotations.
                'scanDir' => [
                    Yii::getAlias('@app/controllers/v1'),
                    Yii::getAlias('@app/models/v1'),
                ],
            ],
            'error' => [
                'class' => 'yii\web\ErrorAction',
            ],
        ];
    }
}
