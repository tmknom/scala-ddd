package resources.builder

import domains.tweet.TweetEntity

// scalastyle:off
object TweetEntityBuilder {
  def one: TweetEntity = {
    TweetEntity(
      None,
      1234,
      "sample_userScreenName",
      "sample_text",
      10,
      100,
      "ja",
      DateTimeBuilder.fixedJST
    )
  }
}
