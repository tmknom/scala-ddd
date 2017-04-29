package infrastructures.article

import base.scalatest.DatabaseSpec
import domains.article.{ArticleEntity, ArticleRepository}
import org.scalatest.concurrent.ScalaFutures
import play.api.Application

@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter", "org.wartremover.warts.Null", "org.wartremover.warts.TraversableOps"))
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

  "listAll" should {
    "success" in {
      val articleEntity = ArticleEntity(None, "sample_title", "sample_url")
      whenReady(articleRepository.insert(articleEntity)) { _ =>
        whenReady(articleRepository.listAll()) { actual =>
          // 件数チェック
          actual must have size 1

          // Entityの属性をチェック
          actual.head.id must not be null // scalastyle:ignore
          actual.head.title mustBe articleEntity.title
          actual.head.url mustBe articleEntity.url

          // Entityの属性をチェック：プロパティチェックの構文で書いた場合
          // こちらのほうが見やすい気はするが、属性名を 'title みたいな変な構文で書かないといけない
          // http://www.scalatest.org/user_guide/using_matchers#checkingArbitraryProperties
          //
          // この書き方だと、型が分からないので、IntelliJが助けてくれない
          // 属性名をリファクタリングする時に、追従してくれないため、使わないほうが良さそう
          actual.head must have (
            'title (articleEntity.title),
            'url (articleEntity.url)
          )
        }
      }
    }
  }
}
