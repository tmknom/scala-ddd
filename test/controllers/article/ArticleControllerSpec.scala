package controllers.article

import domains.article.{ArticleEntity, ArticleRepository}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.inject._
import play.api.inject.guice._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ArticleControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar with ScalaFutures {
  "index" when {
    "Some object" should {
      val articleRepository = mock[ArticleRepository]
      val articleEntities: Seq[Future[ArticleEntity]] = Seq(Future {
        ArticleEntity(None, "sample_title", "sample_url")
      })
      val articleVector: Future[Seq[ArticleEntity]] = Future.sequence(articleEntities)
      when(articleRepository.listAll()).thenReturn(articleVector)

      val mockApp = new GuiceApplicationBuilder().
        overrides(bind[ArticleRepository].toInstance(articleRepository)).
        build

      "success" in {
        val json = route(mockApp, FakeRequest(GET, "/articles")).get
        contentAsString(json) mustBe "\"sample_title\"" // """sample_title"""
      }
    }
  }
}
