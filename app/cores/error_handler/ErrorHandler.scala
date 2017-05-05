package cores.error_handler

import javax.inject.{Inject, Provider, Singleton}

import cores.error_handler.internal.{ErrorLogger, ErrorNotification, ErrorRenderer}
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.mvc.Results.InternalServerError
import play.api.mvc.{RequestHeader, Result}
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
    * 本番環境でサーバーエラーが発生したときに実行
    *
    * @param request   リクエストヘッダー
    * @param exception スローされた例外
    */
  override protected def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    ErrorNotification.notify(request, exception)
    Future.successful(InternalServerError(
      ErrorRenderer.render(exception, INTERNAL_SERVER_ERROR)
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
