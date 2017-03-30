package services.article

import javax.inject.{Inject, Singleton}

import domains.article.{ArticleEntity, ArticleRepository}

import scala.concurrent.Future

trait ArticleService {
  def listAll(): Future[Seq[ArticleEntity]]
}

@Singleton
class ArticleServiceImpl @Inject() (articleRepository: ArticleRepository) extends ArticleService {
  override def listAll(): Future[Seq[ArticleEntity]] = {
    articleRepository.listAll()
  }
}
