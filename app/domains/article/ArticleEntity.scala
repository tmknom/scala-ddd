package domains.article

import spray.json._

final case class ArticleEntity(
                                id: Option[Int],
                                title: String,
                                url: String
                              )

object ArticleEntityJsonProtocol extends DefaultJsonProtocol {
  implicit val format: RootJsonFormat[ArticleEntity] = jsonFormat3(ArticleEntity)
}
