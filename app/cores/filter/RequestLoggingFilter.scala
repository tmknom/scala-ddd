package cores.filter

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import cores.filter.internal.{RequestLogger, RequestTime}
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
    RequestLogger.logStart(requestHeader)

    nextFilter(requestHeader).map { result =>
      val requestTime = RequestTime(System.currentTimeMillis - startTime)
      RequestLogger.logEnd(requestHeader, result, requestTime)
      result.withHeaders("X-Request-Time" -> requestTime.value.toString)
    }
  }
}
