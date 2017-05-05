package cores.error_handler.internal

import play.api.libs.json.{JsObject, Json}

/**
  * エラーレスポンス用のJSONを生成するクラス
  */
private[error_handler] object ErrorRenderer {
  /**
    * エラーレスポンス用のJSONを生成
    *
    * @param throwable  スローされた例外
    * @param statusCode HTTPステータスコード
    * @return エラーJSON
    */
  def render(throwable: Throwable, statusCode: Int): JsObject = {
    Json.obj(
      "errors" -> Json.arr(
        Json.obj(
          "statusCode" -> statusCode,
          "exception" -> createExceptionJson(throwable)
        )
      )
    )
  }

  /**
    * 例外をJSON化
    *
    * レスポンスに例外情報を含めることで、ちょっとだけデバッグが捗ると思われる。
    * 内部APIだからこそできる荒業。APIとしてはなくてもよい。
    *
    * @param throwable スローされた例外
    * @return JSON
    */
  private def createExceptionJson(throwable: Throwable): JsObject = {
    Json.obj(
      "className" -> throwable.getClass.getSimpleName,
      "classFullName" -> throwable.getClass.getName,
      "message" -> throwable.getMessage,
      "stackTrace" -> throwable.getStackTrace.map(element => element.toString)
    )
  }
}
