package services.article

import javax.inject.{Inject, Singleton}

import domains.article.{ArticleEntity, ArticleRepository}

trait ArticleService {
  def listAll(): Seq[ArticleEntity]
}

@Singleton
class ArticleServiceImpl @Inject()(articleRepository: ArticleRepository) extends ArticleService {
  override def listAll(): Seq[ArticleEntity] = {
    articleRepository.listAll()
  }
}
