package services.crawler

import domains.tweet.{TweetEntity, TweetRepository, TwitterApi}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import resources.builder.TweetEntityBuilder

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
      val tweetEntity = TweetEntityBuilder.one

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
