package domains.article

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
final case class ArticleEntity(
                                id: Option[Int] = None,
                                title: String,
                                url: String
                              )
