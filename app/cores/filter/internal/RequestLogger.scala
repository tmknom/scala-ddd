package cores.filter.internal

import cores.internal.request.RequestIdStore
import net.logstash.logback.marker.Markers._
import play.api.Logger
import play.api.mvc.{RequestHeader, Result}

import scala.collection.JavaConverters._

/**
  * HTTPリクエストの開始／終了のロガー
  */
private[filter] object RequestLogger {

  /**
    * メタ情報のマーカー名
    */
  private val MarkerName = "request"

  /**
    * リクエストスタート時のメッセージ
    */
  private val StartMessage = "start_request"

  /**
    * リクエスト完了時のメッセージ
    */
  private val CompleteMessage = "complete_request"

  /**
    * HTTPリクエストの開始時のログ出力を行う
    *
    * @param requestHeader リクエストヘッダー
    */
  def logStart(requestHeader: RequestHeader): Unit = {
    val request = createStartLog(requestHeader)
    log(request, StartMessage)
  }

  /**
    * HTTPリクエストの終了時のログ出力を行う
    *
    * @param requestHeader リクエストヘッダー
    * @param result        HTTPレスポンス
    * @param requestTime   リクエスト実行時間
    */
  def logEnd(requestHeader: RequestHeader, result: Result, requestTime: RequestTime): Unit = {
    val request = createEndLog(requestHeader, result, requestTime)
    log(request, CompleteMessage)
  }

  private def log(request: Map[String, String], message: String) = {
    Logger.logger.info(append(MarkerName, request.asJava), message)
  }

  /**
    * HTTPリクエストの開始時のログメッセージの生成
    *
    * @param requestHeader リクエストヘッダー
    * @return ログメッセージ
    */
  private def createStartLog(requestHeader: RequestHeader): Map[String, String] = {
    Map(
      "request_id" -> RequestIdStore.extract(requestHeader).value,
      "method" -> requestHeader.method,
      "path" -> requestHeader.path,
      "raw_query_string" -> requestHeader.rawQueryString,
      "host" -> requestHeader.host,
      "remote_address" -> requestHeader.remoteAddress
    )
  }

  /**
    * HTTPリクエストの出力時のログメッセージの生成
    *
    * @param requestHeader リクエストヘッダー
    * @param result        HTTPレスポンス
    * @param requestTime   リクエスト実行時間
    * @return ログメッセージ
    */
  private def createEndLog(requestHeader: RequestHeader, result: Result, requestTime: RequestTime): Map[String, String] = {
    Map(
      "request_id" -> RequestIdStore.extract(requestHeader).value,
      "request_time_millis" -> requestTime.value.toString,
      "status_code" -> result.header.status.toString
    )
  }
}
