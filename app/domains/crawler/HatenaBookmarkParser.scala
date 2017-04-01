package domains.crawler

import domains.article.ArticleEntity

trait HatenaBookmarkParser {
  def parse(rss: String): List[ArticleEntity]
}
