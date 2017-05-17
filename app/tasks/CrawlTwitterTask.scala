package tasks

import cores.task.Task
import domains.tweet.TweetEntityJsonProtocol._
import play.api.{Application, Logger}
import services.crawler.TwitterService
import spray.json._

/**
  * Twitter クロールタスク
  *
  * sbt "runf-main tasks.CrawlTwitterTask"
  */
object CrawlTwitterTask extends Task {
  override def execute(app: Application): Unit = {
    val twitterService = instanceOf[TwitterService](app)
    val tweetEntities = twitterService.crawl()
    Logger.info(tweetEntities.toJson.prettyPrint)
  }
}
