package services.crawler

import javax.inject.{Inject, Singleton}

import dispatch._
import domains.article.{ArticleEntity, ArticleRepository}
import domains.crawler.{HatenaBookmarkApi, HatenaBookmarkParser}

trait HatenaBookmarkService {
  def crawl(): Seq[ArticleEntity]
}

@Singleton
class HatenaBookmarkServiceImpl @Inject()(
                                           hatenaBookmarkApi: HatenaBookmarkApi,
                                           hatenaBookmarkParser: HatenaBookmarkParser,
                                           articleRepository: ArticleRepository) extends HatenaBookmarkService {

  override def crawl(): Seq[ArticleEntity] = {
    val response = hatenaBookmarkApi.request()
    val articleEntities = hatenaBookmarkParser.parse(response.apply())
    for (articleEntity <- articleEntities){
      articleRepository.insert(articleEntity)
    }
    articleEntities
  }
}
