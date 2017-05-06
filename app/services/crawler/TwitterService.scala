package services.crawler

import javax.inject.{Inject, Singleton}

import domains.crawler.TwitterApi
import domains.tweet.{TweetEntity, TweetRepository}

trait TwitterService {
  def crawl(): Seq[TweetEntity]
}

@Singleton
final class TwitterServiceImpl @Inject()(
                                          api: TwitterApi,
                                          tweetRepository: TweetRepository) extends TwitterService {

  override def crawl(): Seq[TweetEntity] = {
    val response = api.request()
    response.foreach(tweetRepository.insert)
    response
  }
}
