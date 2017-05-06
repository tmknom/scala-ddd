package infrastructures.tweet

import java.time.{LocalDateTime, ZonedDateTime}

import cores.datetime.DateTimeProvider
import cores.test.scalatest.DatabaseSpec
import domains.tweet.TweetEntity

class TweetRepositoryImplSpec extends DatabaseSpec {

  def sut: TweetRepositoryImpl = new TweetRepositoryImpl()

  private val FixedDateTime = LocalDateTime.of(2016, 12, 31, 23, 59, 59) // scalastyle:ignore

  // scalastyle:off
  val tweetEntity = TweetEntity(
    None,
    1234,
    "sample_userScreenName",
    "sample_text",
    10,
    100,
    "ja",
    ZonedDateTime.of(FixedDateTime, DateTimeProvider.JST)
  )
  // scalastyle:on

  "insert" should {
    "success" in {
      val actual = sut.insert(tweetEntity)
      // 絶対もっとマシな書き方あるだろ。。
      actual > 0 mustBe true
    }
  }

  "listAll" should {
    "success" in {
      sut.insert(tweetEntity)
      val actual = sut.listAll()

      // 件数チェック
      actual must have size 1

      // Entityの属性をチェック
      val entity = actual.head
      entity.id must not be null // scalastyle:ignore
      entity.originId mustBe tweetEntity.originId
      entity.userScreenName mustBe tweetEntity.userScreenName
      entity.text mustBe tweetEntity.text
      entity.retweetCount mustBe tweetEntity.retweetCount
      entity.favoriteCount mustBe tweetEntity.favoriteCount
      entity.lang mustBe tweetEntity.lang
      entity.createdDateTime mustBe tweetEntity.createdDateTime
    }
  }
}
