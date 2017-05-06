package tasks

import cores.task.Task
import domains.article.ArticleEntityJsonProtocol._
import play.api.{Application, Logger}
import services.crawler.TwitterService
import spray.json._

/**
  * Twitter クロールタスク
  *
  * activator "runf-main tasks.CrawlTwitterTask"
  */
object CrawlTwitterTask extends Task {
  override def execute(app: Application): Unit = {
    val twitterService = instanceOf[TwitterService](app)
    val articleEntities = twitterService.crawl()
    Logger.info(articleEntities.toJson.prettyPrint)
  }
}
