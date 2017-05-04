package cores.traceable

import play.api.libs.concurrent.Execution

import scala.concurrent.ExecutionContext

/**
  * The standard [[play.api.libs.concurrent.Execution.defaultContext]] loses the MDC context.
  *
  * This custom [[ExecutionContext]] propagates the MDC context, so that the request
  * and the correlation IDs can be logged.
  */
private[traceable] object TraceableExecution {

  object Implicits {
    implicit def defaultContext: ExecutionContext = TraceableExecution.defaultContext
  }

  def defaultContext: ExecutionContext = TraceableExecutionContext.fromThread(Execution.defaultContext)

}
