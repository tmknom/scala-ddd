package infrastructures.adapters.crawler

import dispatch._
import Defaults._
import javax.inject.Singleton

import domains.crawler.HatenaBookmarkAdapter

import scala.xml.XML

@Singleton
class HatenaBookmarkAdapterImpl extends HatenaBookmarkAdapter {
  private val RSS_URL = "http://b.hatena.ne.jp/entrylist/it?sort=hot&threshold=30&mode=rss"
  private val FAKE_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5)"

  override def crawl(): Unit = {
    val rss = request().apply()
    val parsedRss = parseRss(rss)
    println(parsedRss)
  }

  private def parseRss(rss: String): String = {
    val xml = XML.loadString(rss)
    val items = xml \ "item"
    var result = ""
    for(item <- items){
      val title = item \ "title"
      val url = item \ "link"
      result += title.text + " : " + url.text + "\n"
    }
    result
  }

  private def request(): Future[String] = {
    val svc = url(RSS_URL)
      .setHeader("User-Agent", FAKE_USER_AGENT)
    Http(svc OK as.String)
  }
}
