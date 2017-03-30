package domains.article

case class ArticleEntity(
                          id: Option[Int] = None,
                          title: String,
                          url: String
                        )
