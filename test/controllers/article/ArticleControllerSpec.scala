package controllers.article

import base.scalatest.ControllerSpec
import domains.article.{ArticleEntity, ArticleRepository}
import org.mockito.Mockito._
import play.api.Application
import play.api.inject._
import play.api.inject.guice._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ArticleControllerSpec extends ControllerSpec {
  "index" when {
    "Some object" should {
      // モックの作成
      val mockApp = MockBuilder.build()

      "success" in {
        val json = route(mockApp, FakeRequest(GET, "/articles")).get
        contentAsString(json) mustBe "\"sample_title\"" // """sample_title"""
      }
    }
  }

  private object MockBuilder {
    def build(): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[ArticleRepository].toInstance(articleRepository())).
        build
    }

    private def articleRepository(): ArticleRepository = {
      // Mockが返す値を作成
      val articleEntities: Seq[Future[ArticleEntity]] = Seq(Future {
        ArticleEntity(None, "sample_title", "sample_url")
      })
      val articleVector: Future[Seq[ArticleEntity]] = Future.sequence(articleEntities)

      // Mockが返す値をセット
      val articleRepository = mock[ArticleRepository]
      when(articleRepository.listAll()).thenReturn(articleVector)
      articleRepository
    }
  }

//  def createApplicationMock[T <: AnyRef: ClassTag](mock: T): Application = {
//    // ClassTag については下記参照
//    // http://www.ne.jp/asahi/hishidama/home/tech/scala/classtag.html
//    //
//    // 魔法感半端ないので、プロダクトコードではあまり使わないほうがいい気がする
//    // ジェネリクスのtype erasureについて知らないと意味不明だし。。
//    new GuiceApplicationBuilder().
//      overrides(bind[T].toInstance(mock)).
//      build
//  }
}
