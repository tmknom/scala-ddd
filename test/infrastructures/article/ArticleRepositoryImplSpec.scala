package infrastructures.article

import cores.test.scalatest.DatabaseSpec
import domains.article.{ArticleEntity, ArticleRepository}
import play.api.Application

@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter", "org.wartremover.warts.Null", "org.wartremover.warts.TraversableOps"))
class ArticleRepositoryImplSpec extends DatabaseSpec {
  def articleRepository(implicit app: Application): ArticleRepository = Application.instanceCache[ArticleRepositoryImpl].apply(app)

  "insert" should {
    "success" in {
      val articleEntity = ArticleEntity(None, "sample_title", "sample_url")
      val actual = articleRepository.insert(articleEntity)
      // 絶対もっとマシな書き方あるだろ。。
      actual > 0 mustBe true
    }
  }

  "listAll" should {
    "success" in {
      val articleEntity = ArticleEntity(None, "sample_title", "sample_url")
      articleRepository.insert(articleEntity)
      val actual = articleRepository.listAll()

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
      actual.head must have(
        'title (articleEntity.title),
        'url (articleEntity.url)
      )
    }
  }
}
