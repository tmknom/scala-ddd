package cores.execution_context

import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * コントローラのアクション内でロギングを実行した際に
  * MDC で付与した値を使えるようにするアクション
  *
  * 下記のように、アクションメソッド定義時に [[play.api.mvc.Action]] ではなく、本クラスを指定して使う。
  *
  * def index: = MDCAction { ... }
  */
object MDCAction extends ActionBuilder[Request] {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    block(request)
  }

  override protected def executionContext: ExecutionContext = MDCHttpExecution.defaultContext
}
