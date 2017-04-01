package services.crawler

import javax.inject.{Inject, Singleton}

import domains.article.{ArticleEntity, ArticleRepository}
import domains.crawler.{HatenaBookmarkApi, HatenaBookmarkParser}

trait CrawlRssService {
  def perform(): List[ArticleEntity]
}

@Singleton
class CrawlRssServiceImpl @Inject() (hatenaBookmarkApi: HatenaBookmarkApi, hatenaBookmarkParser: HatenaBookmarkParser, articleRepository: ArticleRepository) extends CrawlRssService {
  override def perform(): List[ArticleEntity] = {
    val response = hatenaBookmarkApi.request()
    val articleEntities = hatenaBookmarkParser.parse(response)
    for (articleEntity <- articleEntities){
      articleRepository.insert(articleEntity)
    }
    articleEntities
  }
}
