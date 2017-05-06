package infrastructures.tweet

import cores.test.scalatest.DatabaseSpec
import resources.builder.TweetEntityBuilder

class TweetRepositoryImplSpec extends DatabaseSpec {

  private def sut: TweetRepositoryImpl = new TweetRepositoryImpl()

  private val tweetEntity = TweetEntityBuilder.one

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
