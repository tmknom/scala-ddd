package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import play.api._

@Singleton
class CrawlRssController @Inject() extends Controller {

  def create = Action {
    Ok(Json.toJson(Map("status" -> "OK", "method" -> "post")))
  }

}
