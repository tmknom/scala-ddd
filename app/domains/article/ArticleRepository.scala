package domains.article

trait ArticleRepository {
  def listAll(): Seq[ArticleEntity]
  def search(query: String): Seq[ArticleEntity]

  def insert(article: ArticleEntity): Long
}
