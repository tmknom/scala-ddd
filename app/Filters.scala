import javax.inject._

import cores.filter.{MDCFilter, RequestLoggingFilter}
import play.api._
import play.api.http.HttpFilters
import play.api.mvc._

/**
 * This class configures filters that run on every request. This
 * class is queried by Play to get a list of filters.
 *
 * Play will automatically use filters from any class called
 * `Filters` that is placed the root package. You can load filters
 * from a different class by adding a `play.http.filters` setting to
 * the `application.conf` configuration file.
 *
 * @param env Basic environment settings for the current application.
 * @param exampleFilter A demonstration filter that adds a header to
 * each response.
 */
@Singleton
class Filters @Inject() (
  env: Environment,
  mdcFilter: MDCFilter,
  requestLoggingFilter: RequestLoggingFilter) extends HttpFilters {

  override val filters: Seq[Filter] = {
    /**
      * 【重要】
      * MDCFilter は必ず最初に読み込むこと！
      * 順番を変えると、ログ出力時に MDC の値が使われない可能性がある
      *
      * @todo というか、この順番を守っても、リクエスト終了時のログで、たまに出なくなる。。
      *       MDCFilter と RequestLoggingFilter は分けちゃダメかもしれない。。。
      */
    Seq(mdcFilter, requestLoggingFilter)
  }

}
