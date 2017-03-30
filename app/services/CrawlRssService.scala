package services

import javax.inject.{Inject, Singleton}

import domains.article.{ArticleEntity, ArticleRepository}
import domains.crawler.HatenaBookmarkAdapter

import scala.concurrent.ExecutionContext.Implicits.global

trait CrawlRssService {
  def perform(): List[ArticleEntity]
}

@Singleton
class CrawlRssServiceImpl @Inject() (hatenaBookmarkAdapter: HatenaBookmarkAdapter, articleRepository: ArticleRepository) extends CrawlRssService {
  override def perform(): List[ArticleEntity] = {
    val articleEntities = hatenaBookmarkAdapter.crawl()
    for (articleEntity <- articleEntities){
      articleRepository.insert(articleEntity)
    }
    articleEntities
  }
}
