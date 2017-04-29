package infrastructures.article

import domains.article.{ArticleEntity, ArticleRepository}
import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapper}

class ArticleRepositoryImpl extends SkinnyCRUDMapper[ArticleEntity] with ArticleRepository {
  override lazy val defaultAlias: Alias[ArticleEntity] = createAlias("a")
  override lazy val tableName: String = "articles"
  private[this] lazy val a = defaultAlias

  override def extract(rs: WrappedResultSet, rn: ResultName[ArticleEntity]): ArticleEntity = ArticleEntity(
    id = Some(rs.int(rn.id)),
    title = rs.string(rn.title),
    url = rs.string(rn.url)
  )

  override def listAll(): Seq[ArticleEntity] = findAll()

  override def insert(articleEntity: ArticleEntity): Long = createWithNamedValues(
    column.title -> articleEntity.title,
    column.url -> articleEntity.url
  )
}
