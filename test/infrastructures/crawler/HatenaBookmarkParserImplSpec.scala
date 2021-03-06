package infrastructures.crawler

import cores.test.fixture.FixtureLoader
import org.scalatestplus.play.PlaySpec
import resources.fixture.FixturePath

@SuppressWarnings(Array("org.wartremover.warts.TraversableOps"))
class HatenaBookmarkParserImplSpec extends PlaySpec {
  private val sut = new HatenaBookmarkParserImpl()

  "parse" should {
    "success" in {
      val rss = FixtureLoader.load(FixturePath.HatenaBookmarkRss)
      val actual = sut.parse(rss)

      actual.size mustBe 30
      actual.head.title mustBe "今年もやっぱり来ちゃったよ！　2017年エイプリルフールまとめ - ねとらぼ"
      actual.head.url mustBe "http://nlab.itmedia.co.jp/nl/articles/1704/01/news003.html"
    }
  }
}
