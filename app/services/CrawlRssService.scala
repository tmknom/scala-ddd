package services

import javax.inject.{Inject, Singleton}

import domains.crawler.HatenaBookmarkAdapter

trait CrawlRssService {
  def perform(): Map[String, String]
}

@Singleton
class CrawlRssServiceImpl @Inject() (hatenaBookmarkAdapter: HatenaBookmarkAdapter) extends CrawlRssService {
  override def perform(): Map[String, String] = {
    hatenaBookmarkAdapter.crawl()
    Map("status" -> "OK", "method" -> "post")
  }
}
