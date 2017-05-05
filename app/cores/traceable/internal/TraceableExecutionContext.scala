package cores.traceable.internal

import java.util.Map

import org.slf4j.MDC

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.util.control.Exception.{Catch, ultimately}

/**
  * slf4j provides a MDC [[http://logback.qos.ch/manual/mdc.html Mapped Diagnostic Context]]
  * based on a [[ThreadLocal]]. In an asynchronous environment, the callbacks can be called
  * in another thread, where the local thread variable does not exist anymore.
  *
  * This execution context fixes this problem:
  * it propagates the MDC from the caller's thread to the callee's one.
  *
  * @see http://qiita.com/castersupermild/items/b5682828307430e2cc9a
  * @see http://yanns.github.io/blog/2014/05/04/slf4j-mapped-diagnostic-context-mdc-with-play-framework/
  */
private[internal] object TraceableExecutionContext {

  /**
    * Create an MDCHttpExecutionContext with values from the current thread.
    */
  def fromThread(delegate: ExecutionContext): ExecutionContextExecutor =
    new TraceableExecutionContext(MDC.getCopyOfContextMap, delegate)
}

/**
  * Manages execution to ensure that the given MDC context are set correctly
  * in the current thread. Actual execution is performed by a delegate ExecutionContext.
  */
private[internal] final class TraceableExecutionContext(mdcContext: Map[String, String], delegate: ExecutionContext) extends ExecutionContextExecutor {
  def execute(runnable: Runnable): Unit = delegate.execute(new Runnable {
    def run() {
      val oldMDCContext = MDC.getCopyOfContextMap
      setContextMap(mdcContext)
      withResetContext(oldMDCContext) {
        runnable.run()
      }
    }
  })

  private[this] def setContextMap(context: Map[String, String]) {
    Option(context) match {
      case Some(c) => MDC.setContextMap(c)
      case None => MDC.clear()
    }
  }

  private def withResetContext(context: Map[String, String]): Catch[Unit] = {
    ultimately[Unit] {
      setContextMap(context)
    }
  }

  def reportFailure(t: Throwable): Unit = delegate.reportFailure(t)
}
