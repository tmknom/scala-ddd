package cores.task.internal

import play.api.Play

private[task] object ExecutionStart {
  def start(execution: Execution): Unit = {
    val app = ApplicationInitializer.initialize()
    try {
      execution.execute(app)
    } finally {
      Play.stop(app)
    }
  }
}
