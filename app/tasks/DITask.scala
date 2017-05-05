package tasks

import cores.task.Task
import domains.article.ArticleEntityJsonProtocol._
import play.api.{Application, Logger}
import services.article.ArticleService
import spray.json._

object DITask extends Task {
  override def execute(app: Application): Unit = {
    Logger.info("hello, di!")
    val articleService = instanceOf[ArticleService](app)
    val result = articleService.listAll().toJson.prettyPrint
    Logger.info(result)
  }
}
