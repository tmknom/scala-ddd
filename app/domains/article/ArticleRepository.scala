package domains.article

trait ArticleRepository {
  def listAll(): Seq[ArticleEntity]

  def insert(article: ArticleEntity): Long
}
