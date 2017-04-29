package services.article

import domains.article.{ArticleEntity, ArticleRepository}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder

class ArticleServiceSpec extends PlaySpec with MockitoSugar with ScalaFutures {
  "listAll" should {
    val sut: ArticleService = Application.instanceCache[ArticleService].apply(MockBuilder.build())

    // テスト実行
    "success" in {
      val actual = sut.listAll()
      actual.size mustBe 1
    }
  }

  private object MockBuilder {
    def build(): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[ArticleRepository].toInstance(articleRepository())).
        build
    }

    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    private def articleRepository(): ArticleRepository = {
      // Mockが返す値を作成
      val articleEntities: Seq[ArticleEntity] = Seq(ArticleEntity(None, "sample_title", "sample_url"))

      // Mockが返す値をセット
      val articleRepository = mock[ArticleRepository]
      when(articleRepository.listAll()).thenReturn(articleEntities)
      articleRepository
    }
  }

}
