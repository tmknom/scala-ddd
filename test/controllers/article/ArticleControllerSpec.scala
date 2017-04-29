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

/**
  * OptionPartial を抑制する理由
  *
  * 抑制しない場合「Option#get is disabled - use Option#fold instead」という警告が出る。
  * プロダクトコードでは確かに、Option 型で get メソッドを使わず、fold メソッドを使うというのは良い習慣に思える。
  *
  * 一方で、コントローラのテストでは route(app, FakeRequest(GET, "/any/url")).get というイディオムがよく出てくる。
  * コントローラのテストでは、fold メソッドなどを使うと逆にテストの見通しが悪くなるように見える。
  *
  * よって、コントローラのテストでは明示的に OptionPartial を抑制することにした。
  * できれば、コントローラのテストだけ OptionPartial を勝手に抑制するようにしたい。
  */
@SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
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
        contentAsString(json) mustBe "\"empty\""
      }
    }

    "Some object" should {
      // モックの作成
      val mockApp = MockBuilder.build(MockBuilder.some)
      // テスト実行
      "success" in {
        val json = route(mockApp, FakeRequest(GET, "/articles")).get

        status(json) mustBe OK
        contentAsString(json) mustBe "\"sample_title\"" // """sample_title"""
      }
    }
  }

  private object MockBuilder {
    val empty = Seq.empty[Future[ArticleEntity]]
    val some = Seq(Future {
      ArticleEntity(None, "sample_title", "sample_url")
    })

    def build(articleEntities: Seq[Future[ArticleEntity]]): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[ArticleRepository].toInstance(articleRepository(articleEntities))).
        build
    }

    private def articleRepository(articleEntities: Seq[Future[ArticleEntity]]): ArticleRepository = {
      // Mockが返す値を作成
      val articleVector: Future[Seq[ArticleEntity]] = Future.sequence(articleEntities)

      // Mockが返す値をセット
      val articleRepository = mock[ArticleRepository]
      when(articleRepository.listAll()).thenReturn(articleVector)
      articleRepository
    }
  }
}
