package tasks

import cores.task.Task
import domains.article.ArticleEntity
import domains.article.ArticleEntityJsonProtocol._
import play.api.{Application, Logger}
import spray.json._

object HelloTask extends Task {
  override def execute(app: Application): Unit = {
    val result = ArticleEntity(Option(1), "hoge", "huga").toJson.prettyPrint
    Logger.info("hello, task!")
    Logger.info(result)
  }
}
