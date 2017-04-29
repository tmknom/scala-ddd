package domains.article

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
case class ArticleEntity(
                          id: Option[Int] = None,
                          title: String,
                          url: String
                        )
