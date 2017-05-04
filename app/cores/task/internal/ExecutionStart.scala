package cores.task.internal

import cores.log.MdcKey
import cores.request.RequestId
import org.slf4j.MDC
import play.api.Play

private[task] object ExecutionStart {
  def start(execution: Execution): Unit = {
    MDC.put(MdcKey.RequestId, RequestId.initialize().value)
    val app = ApplicationInitializer.initialize()
    try {
      execution.execute(app)
    } finally {
      Play.stop(app)
      MDC.remove(MdcKey.RequestId)
    }
  }
}
