package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import services.article.ArticleService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ArticleController @Inject()(articleService: ArticleService) extends Controller {

  def index = Action.async {
    val future = articleService.listAll()
    future.map {
      articleEntities => {
        for (articleEntity <- articleEntities) {
          println("hoge: " + articleEntity.title + " : " + articleEntity.url)
        }
        Ok(Json.toJson(articleEntities.head.title))
      }
    }
  }

}
