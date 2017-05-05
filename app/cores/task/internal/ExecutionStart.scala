package cores.task.internal

import cores.log.MdcKey
import cores.request.RequestId
import org.slf4j.MDC
import play.api.{Application, Logger, Play}

import scala.util.control.Exception.{Catch, ultimately}

/**
  * アプリケーションの実行
  *
  * クラス名は play の [[play.core.server.ProdServerStart]] から拝借。
  * 実装自体もかなり参考にしている。
  *
  * @see https://github.com/playframework/playframework/blob/2.5.x/framework/src/play-server/src/main/scala/play/core/server/ProdServerStart.scala
  */
private[task] object ExecutionStart {
  private val startTime: Long = System.currentTimeMillis()

  def start(execution: Execution): Unit = {
    val app: Application = startApplication()
    withCleanup(app) {
      execution.execute(app)
    }
  }

  private def startApplication(): Application = {
    MDC.put(MdcKey.RequestId, RequestId.initialize().value)
    Logger.info("Starting application.")
    ApplicationInitializer.initialize()
  }

  private def withCleanup(app: Application): Catch[Unit] = {
    ultimately[Unit] {
      Play.stop(app)
      val endTime: Float = (System.currentTimeMillis() - startTime) / 1000f
      Logger.info(s"Stopping application, total time: ${"%.1f".format(endTime)} s.")
      MDC.remove(MdcKey.RequestId)
    }
  }
}
