package services.crawler

import javax.inject.{Inject, Singleton}

import domains.article.{ArticleEntity, ArticleRepository}
import domains.crawler.TwitterApi

trait TwitterService {
  def crawl(): Seq[ArticleEntity]
}

@Singleton
final class TwitterServiceImpl @Inject()(
                                          api: TwitterApi,
                                          articleRepository: ArticleRepository) extends TwitterService {

  override def crawl(): Seq[ArticleEntity] = {
    val response = api.request()
    Seq.empty[ArticleEntity]
  }
}
