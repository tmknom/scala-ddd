package cores.task

import cores.task.internal.{Execution, ExecutionStart}

trait Task extends App with Execution {
  // https://github.com/playframework/playframework/blob/2.5.x/framework/src/play-server/src/main/scala/play/core/server/ProdServerStart.scala
  ExecutionStart.start(this)
}
