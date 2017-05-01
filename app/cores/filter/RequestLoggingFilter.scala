package cores.filter

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import cores.filter.internal.{RequestLogger, RequestTime}
import cores.request.RequestHeaderTagName
import org.slf4j.MDC
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * HTTPリクエストとレスポンスをロギングするクラス
  *
  * @see https://www.playframework.com/documentation/2.5.x/ScalaHttpFilters#A-simple-logging-filter
  */
@Singleton
@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
final class RequestLoggingFilter @Inject()(implicit override val mat: Materializer,
                                           exec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis
    MDC.put(MDCKey.RequestId, requestHeader.tags(RequestHeaderTagName.RequestId))
    RequestLogger.logStart(requestHeader)

    nextFilter(requestHeader).map { result =>
      // ココにも書かないと、MDC経由でログが出力されない（なぜだ。。）
      MDC.put(MDCKey.RequestId, requestHeader.tags(RequestHeaderTagName.RequestId))

      val requestTime = RequestTime(System.currentTimeMillis - startTime)
      RequestLogger.logEnd(requestHeader, result, requestTime)

      // MDCは内部的にスレッドローカルを使って実装されている
      // そのため、忘れずに削除しないとメモリリークする可能性がある
      MDC.remove(MDCKey.RequestId)
      result.withHeaders("X-Request-Time" -> requestTime.value.toString)
    }
  }
}

/**
  * logback の MDC(Mapped Diagnostic Context) のキー名を定義
  *
  * logback.xml の pattern 部に %X{requestId} のように記述する
  *
  * @see https://logback.qos.ch/manual/mdc.html
  */
private[filter] object MDCKey {
  val RequestId: String = "requestId"
}
