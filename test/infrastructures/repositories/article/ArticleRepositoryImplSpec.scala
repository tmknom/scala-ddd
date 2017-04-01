package infrastructures.repositories.article

import base.scalatest.DatabaseSpec
import domains.article.{ArticleEntity, ArticleRepository}
import org.scalatest.concurrent.ScalaFutures
import play.api.Application

class ArticleRepositoryImplSpec extends DatabaseSpec with ScalaFutures {
  def articleRepository(implicit app: Application): ArticleRepository = Application.instanceCache[ArticleRepositoryImpl].apply(app)

  "insert" should {
    "success" in {
      val articleEntity = ArticleEntity(None, "sample_title", "sample_url")
      whenReady(articleRepository.insert(articleEntity)) { actual =>
        actual mustBe((): Unit)
      }
    }
  }
}
