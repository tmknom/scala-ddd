package infrastructures.repositories.article

import scala.concurrent.Future
import javax.inject.Inject

import domains.article.{ArticleEntity, ArticleRepository}
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

class ArticleRepositoryImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with ArticleRepository {
  import driver.api._

  private val Articles = TableQuery[ArticlesTable]

  override def listAll(): Future[Seq[ArticleEntity]] = db.run(Articles.result)

  override def insert(articleEntity: ArticleEntity): Future[Unit] = db.run(Articles += articleEntity).map { _ => () }

  private class ArticlesTable(tag: Tag) extends Table[ArticleEntity](tag, "articles") {
    def id = column[Int]("id", O.PrimaryKey)
    def title = column[String]("title")
    def url = column[String]("url")

    def * = (id.?, title, url) <> (ArticleEntity.tupled, ArticleEntity.unapply _)
  }
}
