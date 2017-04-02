package infrastructures.crawler

import base.scalatest.utility.FixtureLoader
import fixtures.FixturePath
import org.scalatestplus.play.PlaySpec

class HatenaBookmarkParserImplSpec extends PlaySpec {
  val sut = new HatenaBookmarkParserImpl()

  "parse" should {
    "success" in {
      val rss = FixtureLoader.load(FixturePath.HATENA_BOOKMARK_RSS)
      val actual = sut.parse(rss)

      actual.size mustBe 30
      actual.head.title mustBe "今年もやっぱり来ちゃったよ！　2017年エイプリルフールまとめ - ねとらぼ"
      actual.head.url mustBe "http://nlab.itmedia.co.jp/nl/articles/1704/01/news003.html"
    }
  }
}
