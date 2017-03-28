package services

import javax.inject.Singleton

trait CrawlRssService {
  def perform(): Map[String, String]
}

@Singleton
class CrawlRssServiceImpl extends CrawlRssService {
  override def perform(): Map[String, String] = {
    Map("status" -> "OK", "method" -> "post")
  }
}
