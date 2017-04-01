package infrastructures.parser.crawler

import javax.inject.Singleton

import dispatch._
import domains.article.ArticleEntity
import domains.crawler.HatenaBookmarkParser

import scala.collection.mutable.ListBuffer
import scala.xml.{NodeSeq, XML}

@Singleton
class HatenaBookmarkParserImpl extends HatenaBookmarkParser {

  override def parse(rss: Future[String]): List[ArticleEntity] = {
    var result = ListBuffer.empty[ArticleEntity]
    for (item <- items(rss.apply())) {
      val title = (item \ "title").text
      val url = (item \ "link").text

      val articleEntity = ArticleEntity(None, title, url)
      result += articleEntity
    }
    result.toList
  }

  private def items(string: String): NodeSeq = {
    val xml = XML.loadString(string)
    xml \ "item"
  }
}
