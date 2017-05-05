package cores.filter.internal

import cores.internal.constant.RequestHeaderTagName
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Result}

/**
  * HTTPリクエストの開始／終了のロガー
  */
private[filter] object RequestLogger {

  /**
    * HTTPリクエストの開始時のログ出力を行う
    *
    * @param requestHeader リクエストヘッダー
    */
  def logStart(requestHeader: RequestHeader): Unit = {
    Logger.info(createStartLog(requestHeader))
  }

  /**
    * HTTPリクエストの終了時のログ出力を行う
    *
    * @param requestHeader リクエストヘッダー
    * @param result        HTTPレスポンス
    * @param requestTime   リクエスト実行時間
    */
  def logEnd(requestHeader: RequestHeader, result: Result, requestTime: RequestTime): Unit = {
    Logger.info(createEndLog(requestHeader, result, requestTime))
  }

  /**
    * HTTPリクエストの開始時のログメッセージの生成
    *
    * @param requestHeader リクエストヘッダー
    * @return ログメッセージ
    */
  private def createStartLog(requestHeader: RequestHeader): String = {
    Json.obj(
      "startRequest" -> Json.obj(
        "requestId" -> requestHeader.tags(RequestHeaderTagName.RequestId),
        "method" -> requestHeader.method,
        "path" -> requestHeader.path,
        "rawQueryString" -> requestHeader.rawQueryString,
        "host" -> requestHeader.host,
        "remoteAddress" -> requestHeader.remoteAddress
      )
    ).toString()
  }


  /**
    * HTTPリクエストの出力時のログメッセージの生成
    *
    * @param requestHeader リクエストヘッダー
    * @param result        HTTPレスポンス
    * @param requestTime   リクエスト実行時間
    * @return ログメッセージ
    */
  private def createEndLog(requestHeader: RequestHeader, result: Result, requestTime: RequestTime): String = {
    Json.obj(
      "endRequest" -> Json.obj(
        "requestId" -> requestHeader.tags(RequestHeaderTagName.RequestId),
        "requestTimeMillis" -> requestTime.value,
        "httpStatus" -> result.header.status
      )
    ).toString()
  }
}
