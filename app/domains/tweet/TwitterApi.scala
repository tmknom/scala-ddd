package domains.tweet

trait TwitterApi {
  def request(): Seq[TweetEntity]
}
