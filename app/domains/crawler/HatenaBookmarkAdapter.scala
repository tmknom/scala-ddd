package domains.crawler

import domains.article.ArticleEntity

trait HatenaBookmarkAdapter {
  def crawl(): List[ArticleEntity]
}
