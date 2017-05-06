package services.crawler

import java.time.{LocalDateTime, ZonedDateTime}

import cores.datetime.DateTimeProvider
import domains.tweet.{TweetEntity, TweetRepository, TwitterApi}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder

class TwitterServiceSpec extends PlaySpec with MockitoSugar {
  "crawl" should {
    val sut: TwitterService = Application.instanceCache[TwitterService].apply(MockBuilder.build())

    // テスト実行
    "success" in {
      val actual = sut.crawl()

      actual.size mustBe 1
    }
  }

  private object MockBuilder {
    def build(): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[TwitterApi].toInstance(mockTwitterApi())).
        overrides(bind[TweetRepository].toInstance(mockTweetRepository())).
        build
    }

    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    def mockTwitterApi(): TwitterApi = {
      // Mockが返す値を作成
      val FixedDateTime = LocalDateTime.of(2016, 12, 31, 23, 59, 59) // scalastyle:ignore
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

      // Mockが返す値をセット
      val mockInstance = mock[TwitterApi]
      when(mockInstance.request()).thenReturn(Seq[TweetEntity](tweetEntity))
      mockInstance
    }

    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    def mockTweetRepository(): TweetRepository = {
      // Mockが返す値をセット
      val mockInstance = mock[TweetRepository]
      when(mockInstance.insert(any[TweetEntity])).thenReturn(2)
      mockInstance
    }
  }

}
