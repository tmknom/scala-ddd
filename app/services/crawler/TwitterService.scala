package services.crawler

import javax.inject.{Inject, Singleton}

import domains.tweet.{TweetEntity, TweetRepository, TwitterApi}

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
