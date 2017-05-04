package controllers.article

import cores.test.scalatest.ControllerSpec
import domains.article.{ArticleEntity, ArticleRepository}
import org.mockito.Mockito._
import play.api.Application
import play.api.inject._
import play.api.inject.guice._
import play.api.test.Helpers._
import play.api.test._

class ArticleControllerSpec extends ControllerSpec {
  "index" when {
    // http://www.innovaedge.com/2015/07/01/how-to-use-mocks-in-injected-objects-with-guiceplayscala/
    "Empty object" should {
      // モックの作成
      val mockApp = MockBuilder.build(MockBuilder.empty)
      // テスト実行
      "success" in {
        val json = route(mockApp, FakeRequest(GET, "/articles")).get

        status(json) mustBe OK
        contentAsString(json) mustBe "[]"
      }
    }

    "Some object" should {
      // モックの作成
      val mockApp = MockBuilder.build(MockBuilder.some)
      // テスト実行
      "success" in {
        val json = route(mockApp, FakeRequest(GET, "/articles")).get
        val expected =
          """[{
            |  "title": "sample_title",
            |  "url": "sample_url"
            |}]""".stripMargin

        status(json) mustBe OK
        contentAsString(json) mustBe expected
      }
    }
  }

  private object MockBuilder {
    val empty = Seq.empty[ArticleEntity]
    val some = Seq(ArticleEntity(None, "sample_title", "sample_url"))

    def build(articleEntities: Seq[ArticleEntity]): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[ArticleRepository].toInstance(articleRepository(articleEntities))).
        build
    }

    // よく分からんが警告が出る。。
    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    private def articleRepository(articleEntities: Seq[ArticleEntity]): ArticleRepository = {
      // Mockが返す値をセット
      val articleRepository = mock[ArticleRepository]
      when(articleRepository.listAll()).thenReturn(articleEntities)
      articleRepository
    }
  }

}
