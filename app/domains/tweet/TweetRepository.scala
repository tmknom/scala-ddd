package domains.tweet

trait TweetRepository {
  def listAll(): Seq[TweetEntity]

  def insert(tweet: TweetEntity): Long
}
