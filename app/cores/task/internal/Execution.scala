package cores.task.internal

import play.api._

private[task] trait Execution extends App {
  def execute(app: Application): Unit
}
