package services.crawler

import javax.inject.{Inject, Singleton}

import domains.crawler.TwitterApi
import domains.tweet.TweetEntity

trait TwitterService {
  def crawl(): Seq[TweetEntity]
}

@Singleton
final class TwitterServiceImpl @Inject()(
                                          api: TwitterApi) extends TwitterService {

  override def crawl(): Seq[TweetEntity] = {
    val response = api.request()
    response
  }
}
