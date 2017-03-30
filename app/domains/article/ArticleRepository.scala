package domains.article

import scala.concurrent.Future

trait ArticleRepository {
//  def listAll(): Future[Seq[ArticleEntity]]
  def insert(article: ArticleEntity): Future[Unit]
}
