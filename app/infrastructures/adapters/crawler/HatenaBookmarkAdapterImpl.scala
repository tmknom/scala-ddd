package infrastructures.adapters.crawler

import javax.inject.Singleton

import domains.crawler.HatenaBookmarkAdapter

@Singleton
class HatenaBookmarkAdapterImpl extends HatenaBookmarkAdapter {
  override def crawl(): Unit = {
    println("クロールするよ！")
  }
}
