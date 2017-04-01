package domains.crawler

import dispatch.Future
import domains.article.ArticleEntity

trait HatenaBookmarkParser {
  def parse(rss: Future[String]): List[ArticleEntity]
}
