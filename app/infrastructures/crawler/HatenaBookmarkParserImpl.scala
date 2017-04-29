package infrastructures.crawler

import javax.inject.Singleton

import domains.article.ArticleEntity
import domains.crawler.HatenaBookmarkParser

import scala.xml.{NodeSeq, XML}

@Singleton
class HatenaBookmarkParserImpl extends HatenaBookmarkParser {

  override def parse(rss: String): Seq[ArticleEntity] = {
    items(rss).map(item => {
      val title = (item \ "title").text
      val url = (item \ "link").text
      ArticleEntity(None, title, url)
    })
  }

  private def items(string: String): NodeSeq = {
    val xml = XML.loadString(string)
    xml \ "item"
  }
}
