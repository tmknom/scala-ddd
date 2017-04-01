package infrastructures.api.crawler

import javax.inject.Singleton

import dispatch.{Future, Http, as, url}
import domains.crawler.HatenaBookmarkApi

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HatenaBookmarkApiImpl extends HatenaBookmarkApi {
  private val RSS_URL = "http://b.hatena.ne.jp/entrylist/it?sort=hot&threshold=30&mode=rss"
  private val FAKE_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5)"

  override def request(): Future[String] = {
    // はてなブックマークはユーザエージェントでアクセスが弾かれるケースがあるので、ダミーをセットしている
    val svc = url(RSS_URL).setHeader("User-Agent", FAKE_USER_AGENT)
    Http(svc OK as.String)
  }
}
