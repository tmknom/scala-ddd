package services

import javax.inject.{Inject, Singleton}

import domains.article.ArticleEntity
import domains.crawler.HatenaBookmarkAdapter

trait CrawlRssService {
  def perform(): List[ArticleEntity]
}

@Singleton
class CrawlRssServiceImpl @Inject() (hatenaBookmarkAdapter: HatenaBookmarkAdapter) extends CrawlRssService {
  override def perform(): List[ArticleEntity] = {
    val articleEntities = hatenaBookmarkAdapter.crawl()
    for (articleEntity <- articleEntities){
      println(articleEntity.title + " : " + articleEntity.url)
    }
    articleEntities
  }
}
