package infrastructures.crawler

import javax.inject.Singleton

import dispatch.{Future, Http, as, url}
import domains.crawler.HatenaBookmarkApi

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HatenaBookmarkApiImpl extends HatenaBookmarkApi {
  private val RssUrl = "http://b.hatena.ne.jp/entrylist/it?sort=hot&threshold=30&mode=rss"
  private val FakeUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5)"

  override def request(): Future[String] = {
    // はてなブックマークはユーザエージェントでアクセスが弾かれるケースがあるので、ダミーをセットしている
    val svc = url(RssUrl).setHeader("User-Agent", FakeUserAgent)
    Http(svc OK as.String)
  }
}
