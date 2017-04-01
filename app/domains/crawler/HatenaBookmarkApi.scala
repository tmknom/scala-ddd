package domains.crawler

import dispatch.Future

trait HatenaBookmarkApi {
  def request(): Future[String]
}
