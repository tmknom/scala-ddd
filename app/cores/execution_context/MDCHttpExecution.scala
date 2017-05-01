package cores.execution_context

import play.api.libs.concurrent.Execution

import scala.concurrent.ExecutionContext

/**
  * The standard [[play.api.libs.concurrent.Execution.defaultContext]] loses the MDC context.
  *
  * This custom [[ExecutionContext]] propagates the MDC context, so that the request
  * and the correlation IDs can be logged.
  */
private[execution_context] object MDCHttpExecution {

  object Implicits {
    implicit def defaultContext: ExecutionContext = MDCHttpExecution.defaultContext
  }

  def defaultContext: ExecutionContext = MDCHttpExecutionContext.fromThread(Execution.defaultContext)

}
