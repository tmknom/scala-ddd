package services.article

import javax.inject.{Inject, Singleton}

import domains.article.{ArticleEntity, ArticleRepository}

trait ArticleService {
  def listAll(): Seq[ArticleEntity]
  def search(query: String): Seq[ArticleEntity]
}

@Singleton
class ArticleServiceImpl @Inject()(articleRepository: ArticleRepository) extends ArticleService {
  override def listAll(): Seq[ArticleEntity] = {
    articleRepository.listAll()
  }

  override def search(query: String): Seq[ArticleEntity] = {
    articleRepository.search(query: String)
  }
}
