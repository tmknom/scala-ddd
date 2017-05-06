package domains.crawler

import domains.tweet.TweetEntity

trait TwitterApi {
  def request(): Seq[TweetEntity]
}
