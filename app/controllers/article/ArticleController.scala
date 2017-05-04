package controllers.article

import javax.inject._

import cores.traceable.TraceableAction
import domains.article.ArticleEntity
import play.api.libs.json.Json
import play.api.mvc._
import services.article.ArticleService

@Singleton
class ArticleController @Inject()(articleService: ArticleService) extends Controller {

  def index: Action[AnyContent] = TraceableAction {
    val articleEntities: Seq[ArticleEntity] = articleService.listAll()

    articleEntities.headOption match {
      case Some(articleEntity) => Ok(Json.toJson(articleEntity.title))
      case None => Ok(Json.toJson("empty"))
    }
  }
}
