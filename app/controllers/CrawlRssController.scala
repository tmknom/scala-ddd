package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import play.api._
import services.CrawlRssService

@Singleton
class CrawlRssController @Inject() (crawlRssService: CrawlRssService) extends Controller {

  def create = Action {
    val result = crawlRssService.perform()
    Ok(Json.toJson(result))
  }

}
