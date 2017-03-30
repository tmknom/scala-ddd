package controllers.crawler

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import services.crawler.CrawlRssService

@Singleton
class CrawlRssController @Inject() (crawlRssService: CrawlRssService) extends Controller {

  def create = Action {
    val articleEntities = crawlRssService.perform()
    println(articleEntities.size)

    val result = Map("status" -> "OK", "method" -> "post")
    Ok(Json.toJson(result))
  }

}
