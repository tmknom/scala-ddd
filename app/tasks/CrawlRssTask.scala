package tasks

import cores.task.Task
import domains.article.ArticleEntityJsonProtocol._
import play.api.{Application, Logger}
import services.crawler.HatenaBookmarkService
import spray.json._

/**
  * RSS クロールタスク
  *
  * activator "run-main tasks.CrawlRssTask"
  */
object CrawlRssTask extends Task {
  override def execute(app: Application): Unit = {
    val hatenaBookmarkService = instanceOf[HatenaBookmarkService](app)
    val articleEntities = hatenaBookmarkService.crawl()
    Logger.info(articleEntities.toJson.prettyPrint)
  }
}
