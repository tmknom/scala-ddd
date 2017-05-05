package cores.task.internal

import play.api._

private[task] trait Execution {
  def execute(app: Application): Unit
}
