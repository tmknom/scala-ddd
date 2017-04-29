package controllers.article

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import services.article.ArticleService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ArticleController @Inject()(articleService: ArticleService) extends Controller {

  def index: Action[AnyContent] = Action.async {
    val future = articleService.listAll()
    future.map {
      articleEntities => {
        for (articleEntity <- articleEntities) {
          println("hoge: " + articleEntity.title + " : " + articleEntity.url) // scalastyle:ignore
        }

        if (articleEntities.nonEmpty) {
          Ok(Json.toJson(articleEntities.head.title))
        }else{
          Ok(Json.toJson("empty"))
        }
      }
    }
  }

}
