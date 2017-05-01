package cores.filter

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import cores.request.RequestHeaderTagName
import org.slf4j.MDC
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * logback の MDC にリクエストIDをセットするクラス
  */
@Singleton
@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
final class MDCFilter @Inject()(implicit override val mat: Materializer,
                                exec: ExecutionContext) extends Filter {
  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    MDC.put(MDCKey.RequestId, requestHeader.tags(RequestHeaderTagName.RequestId))

    nextFilter(requestHeader).map { result =>
      // MDCは内部的にスレッドローカルを使って実装されている
      // そのため、忘れずに削除しないとメモリリークする可能性がある
      MDC.remove(MDCKey.RequestId)
      result
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
