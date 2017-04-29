package controllers.crawler

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import services.crawler.HatenaBookmarkService

@Singleton
class CrawlRssController @Inject() (hatenaBookmarkService: HatenaBookmarkService) extends Controller {

  def create: Action[AnyContent] = Action {
    val articleEntities = hatenaBookmarkService.crawl()
    println(articleEntities.size)

    val result = Map("status" -> "OK", "method" -> "post")
    Ok(Json.toJson(result))
  }

}
