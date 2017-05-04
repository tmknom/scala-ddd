package domains.article

import spray.json._

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
final case class ArticleEntity(
                                id: Option[Int] = None,
                                title: String,
                                url: String
                              )

object ArticleEntityJsonProtocol extends DefaultJsonProtocol {
  implicit val format: RootJsonFormat[ArticleEntity] = jsonFormat3(ArticleEntity)
}
