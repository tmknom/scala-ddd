package cores.error_handler

import java.util.UUID
import javax.inject.{Inject, Provider, Singleton}

import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router
import play.api._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Results.InternalServerError

import scala.concurrent.Future

/**
  * エラーハンドラー
  *
  * 例外がスローされると最終的にここでハンドリングを行う。
  * https://www.playframework.com/documentation/2.5.x/ScalaErrorHandling#Extending-the-default-error-handler
  *
  * 例外ハンドラーの責務は3つ
  * ・エラーJSONを返す
  * ・エラーログを出力する
  * ・エラーを通知する
  *
  * @param env The environment for the application.
  * @param config A full configuration set.
  * @param sourceMapper provides source code to be displayed on error pages
  * @param router A router.
  */
@Singleton
class ErrorHandler @Inject() (
                               env: Environment,
                               config: Configuration,
                               sourceMapper: OptionalSourceMapper,
                               router: Provider[Router]
                             ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  /**
    * Invoked in dev mode when a server error occurs.
    *
    * @param request The request that triggered the error.
    * @param exception The exception.
    */
  override protected def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    val id = UUID.randomUUID
    ErrorLogger.log(request, exception, id)
    ErrorResponse.responseInternalServerError(exception, INTERNAL_SERVER_ERROR, id)
  }

  /**
    * Invoked in prod mode when a server error occurs.
    *
    * @param request The request that triggered the error.
    * @param exception The exception.
    */
  override protected def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    val id = UUID.randomUUID
    ErrorLogger.log(request, exception, id)
    ErrorResponse.responseInternalServerError(exception, INTERNAL_SERVER_ERROR, id)
  }
}

/**
  * エラーJSONを返すクラス
  */
object ErrorResponse {
  /**
    * InternalServerError を返す
    *
    * @param throwable The exception.
    * @param statusCode The error status code.
    */
  def responseInternalServerError(throwable: Throwable, statusCode: Int, id: UUID): Future[Result] = {
    Future.successful(InternalServerError(
      createErrorJson(throwable, statusCode, id)
    ))
  }

  private def createErrorJson(throwable: Throwable, statusCode: Int, id: UUID): JsObject = {
    Json.obj(
      "errors" -> Json.arr(
        Json.obj(
          "id" -> id,
          "statusCode" -> statusCode.toString,
          "message" -> throwable.getMessage
        )
      )
    )
  }
}

/**
  * エラーログを出力するクラス
  */
object ErrorLogger {
  /**
    * エラーログを出力する
    *
    * @param request The HTTP request header.
    * @param exception The exception.
    */
  def log(request: RequestHeader, exception: UsefulException, id: UUID): Unit = {
    val message: String = s"$id - Production Error while processing request. Returning $INTERNAL_SERVER_ERROR for ${request.uri}"
    Logger.error(message, exception)
  }
}
