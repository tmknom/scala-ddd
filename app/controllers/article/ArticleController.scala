package controllers.article

import javax.inject._

import cores.traceable.TraceableAction
import cores.validation.FormValidation
import domains.article.ArticleEntity
import domains.article.ArticleEntityJsonProtocol._
import domains.search.SearchQuery
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.article.ArticleService
import spray.json._

@Singleton
class ArticleController @Inject()(articleService: ArticleService) extends Controller {

  def index: Action[AnyContent] = TraceableAction {
    val articleEntities: Seq[ArticleEntity] = articleService.listAll()
    Ok(articleEntities.toJson.prettyPrint)
  }

  def search(query: String): Action[AnyContent] = TraceableAction { implicit request =>
    val searchQuery = FormValidation.validate(searchForm)
    val articleEntities: Seq[ArticleEntity] = articleService.search(searchQuery.value)
    Ok(articleEntities.toJson.prettyPrint)
  }

  val searchForm: Form[SearchQuery] = Form(
    mapping(
      "query" -> nonEmptyText
    )(SearchQuery.apply)(SearchQuery.unapply)
  )
}
