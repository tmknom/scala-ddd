package cores.error_handler.internal

import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.RequestHeader

/**
  * エラー用のロガー
  */
private[error_handler] object ErrorLogger {

  /**
    * エラーログの出力
    *
    * @param requestHeader リクエストヘッダー
    * @param throwable     スローされた例外
    */
  def error(requestHeader: RequestHeader, throwable: Throwable): Unit = {
    val message = createMessage(requestHeader, throwable)
    Logger.logger.error(message)
  }

  /**
    * エラーメッセージの作成
    *
    * @param requestHeader リクエストヘッダー
    * @param throwable     スローされた例外
    */
  private def createMessage(requestHeader: RequestHeader, throwable: Throwable): String = {
    Json.obj(
      "request" -> createRequestHeaderJson(requestHeader),
      "exception" -> createExceptionJson(throwable)
    ).toString()
  }

  /**
    * ログ出力用にリクエストヘッダーをJSON化
    *
    * @param requestHeader リクエストヘッダー
    * @return JSON
    */
  private def createRequestHeaderJson(requestHeader: RequestHeader): JsObject = {
    Json.obj(
      "requestId" -> requestHeader.id,
      "method" -> requestHeader.method,
      "path" -> requestHeader.path,
      "rawQueryString" -> requestHeader.rawQueryString,
      "host" -> requestHeader.host,
      "remoteAddress" -> requestHeader.remoteAddress
    )
  }

  /**
    * ログ出力用に例外をJSON化
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
