package infrastructures.adapters.crawler

import javax.inject.Singleton

import dispatch.Defaults._
import dispatch._
import domains.article.ArticleEntity
import domains.crawler.HatenaBookmarkAdapter

import scala.collection.mutable.ListBuffer
import scala.xml.XML

@Singleton
class HatenaBookmarkAdapterImpl extends HatenaBookmarkAdapter {
  private val RSS_URL = "http://b.hatena.ne.jp/entrylist/it?sort=hot&threshold=30&mode=rss"
  private val FAKE_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5)"

  override def crawl(): List[ArticleEntity] = {
    val rss = request().apply()
    val parsedRss = parseRss(rss)
    parsedRss
  }

  private def parseRss(rss: String): List[ArticleEntity] = {
    val xml = XML.loadString(rss)
    val items = xml \ "item"
    var result = ListBuffer.empty[ArticleEntity]
    for (item <- items) {
      val title = (item \ "title").text
      val url = (item \ "link").text

      val articleEntity = ArticleEntity(None, title, url)
      result += articleEntity
    }
    result.toList
  }

  private def request(): Future[String] = {
    val svc = url(RSS_URL)
      .setHeader("User-Agent", FAKE_USER_AGENT)
    Http(svc OK as.String)
  }
}
