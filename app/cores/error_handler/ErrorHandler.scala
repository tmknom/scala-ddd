package cores.error_handler

import javax.inject.{Inject, Provider, Singleton}

import cores.error_handler.internal.{ErrorLogger, ErrorNotification, ErrorRenderer}
import cores.internal.request.RequestIdStore
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import play.api.mvc.Results.InternalServerError
import play.api.mvc.{RequestHeader, Result, Results}
import play.api.routing.Router

import scala.concurrent.Future

/**
  * エラーハンドラー
  *
  * 例外がスローされると最終的にここでハンドリングを行う。
  * 例外ハンドラーの主な責務は3つ。
  * ・エラーJSONを返す
  * ・エラーログを出力する
  * ・エラーを通知する
  *
  * 本エラーハンドラーを使用するようアプリケーションに組み込むには、明示的に設定ファイルへの記述が必要。
  * 多くの場合 conf/application.conf ファイルに記述することになる。
  * 設定箇所は play.http の errorHandler の項目である。
  *
  * @see https://www.playframework.com/documentation/2.5.x/ScalaErrorHandling#Extending-the-default-error-handler
  * @param env          The environment for the application.
  * @param config       A full configuration set.
  * @param sourceMapper provides source code to be displayed on error pages
  * @param router       A router.
  */
@Singleton
final class ErrorHandler @Inject()(
                                    env: Environment,
                                    config: Configuration,
                                    sourceMapper: OptionalSourceMapper,
                                    router: Provider[Router]
                                  ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  /**
    * クライアントエラーが発生したときに実行
    *
    * @param request    リクエストヘッダー
    * @param statusCode HTTPステータスコード
    * @param message    エラーメッセージ
    */
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    statusCode match {
      case BAD_REQUEST =>
        badRequest(request, message)
      case clientErrorStatusCode if statusCode >= BAD_REQUEST && statusCode < INTERNAL_SERVER_ERROR =>
        clientError(request, message, clientErrorStatusCode)
      case _ =>
        throw new IllegalArgumentException(s"onClientError invoked with non client error status code $statusCode: $message")
    }
  }

  // 必須パラメータエラーか判定するための文字列
  // play のエラーメッセージを直接解析するのは微妙だが、他に手段がないため仕方ない
  // https://github.com/playframework/playframework/blob/master/framework/src/play/src/main/scala/play/core/routing/GeneratedRouter.scala
  private val MissingParameterMessage = "Missing parameter:"

  private def badRequest(requestHeader: RequestHeader, message: String): Future[Result] = {
    // 必須パラメータがない場合、バリデーションエラーとして処理するため、例外をスローし直す
    if (message.contains(MissingParameterMessage)) {
      throw new RuntimeException(message)
    }
    clientError(requestHeader, message, BAD_REQUEST)
  }

  private def clientError(request: RequestHeader, message: String, statusCode: Int): Future[Result] = {
    val requestId = RequestIdStore.extract(request)
    val body = ErrorRenderer.render(message, statusCode, requestId)
    Future.successful(Results.Status(statusCode)(body))
  }

  /**
    * 本番環境でサーバーエラーが発生したときに実行
    *
    * @param request   リクエストヘッダー
    * @param exception スローされた例外
    */
  override protected def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    val requestId = RequestIdStore.extract(request)

    ErrorNotification.notify(request, exception)
    Future.successful(InternalServerError(
      ErrorRenderer.render(exception, INTERNAL_SERVER_ERROR, requestId)
    ))
  }

  /**
    * 開発環境でサーバーエラーが発生したときに実行
    *
    * とりあえず本番環境と同じ処理をすることにしているが、
    * 開発環境だけ、ハンドリング方法を変更することも可能。
    * たぶんデバッグしやすいようにカスタマイズできる余地を残しているんだと思う。
    *
    * @param request   リクエストヘッダー
    * @param exception スローされた例外
    */
  override protected def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    onProdServerError(request, exception)
  }

  /**
    * エラーログの出力
    *
    * UsefulException しかログ出力してくれないが、それでいいんかいなって気持ちになる。
    * このメソッドをオーバーライドするんじゃなくて、フツーにログ出力処理を呼び出したほうがいいかもしれない。
    *
    * @param request         リクエストヘッダー
    * @param usefulException スローされた例外
    */
  override protected def logServerError(request: RequestHeader, usefulException: UsefulException) {
    ErrorLogger.error(request, usefulException)
  }
}
