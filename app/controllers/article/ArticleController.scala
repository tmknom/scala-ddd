package controllers.article

import javax.inject._

import cores.traceable.TraceableAction
import domains.article.ArticleEntity
import domains.article.ArticleEntityJsonProtocol._
import play.api.mvc._
import services.article.ArticleService
import spray.json._

@Singleton
class ArticleController @Inject()(articleService: ArticleService) extends Controller {

  def index: Action[AnyContent] = TraceableAction {
    val articleEntities: Seq[ArticleEntity] = articleService.listAll()
    Ok(articleEntities.toJson.prettyPrint)
  }

  def search(query: String): Action[AnyContent] = TraceableAction {
    val articleEntities: Seq[ArticleEntity] = articleService.search(query)
    Ok(articleEntities.toJson.prettyPrint)
  }
}
