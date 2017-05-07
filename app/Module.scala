import java.time.Clock

import com.google.inject.AbstractModule
import domains.article._
import domains.crawler._
import domains.tweet._
import infrastructures.article._
import infrastructures.crawler._
import infrastructures.tweet._
import services.article._
import services.crawler._

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */

/**
  * NonUnitStatements を抑制している理由
  *
  * オーバーライドしている configure メソッドは AbstractModule クラスでは void で定義されている。
  * よって、返り値を Unit にしているのは正しい。
  *
  * 一方で Scala の言語仕様上、メソッドの最終行の結果が return される挙動をする。
  * そのため、バインドに関わるメソッド（例えば to メソッド）の返り値が void ではない場合に
  * 「Statements must return Unit」という警告が出てしまう。
  *
  * 以上のことを勘案した上で、ここでの返り値は、重要な関心事ではないと判断し、警告を抑制することとした。
  */
@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class Module extends AbstractModule {

  override def configure(): Unit = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)

    // とりあえずレイヤ単位でメソッドを切っておく
    configureServices()
    configureInfrastructures()
  }

  private def configureServices() = {
    bind(classOf[HatenaBookmarkService]).to(classOf[HatenaBookmarkServiceImpl])
    bind(classOf[ArticleService]).to(classOf[ArticleServiceImpl])
    bind(classOf[TwitterService]).to(classOf[TwitterServiceImpl])
  }

  private def configureInfrastructures() = {
    bind(classOf[ArticleRepository]).to(classOf[ArticleRepositoryImpl])
    bind(classOf[HatenaBookmarkApi]).to(classOf[HatenaBookmarkApiImpl])
    bind(classOf[HatenaBookmarkParser]).to(classOf[HatenaBookmarkParserImpl])
    bind(classOf[TweetRepository]).to(classOf[TweetRepositoryImpl])
    bind(classOf[TwitterApi]).to(classOf[TwitterApiImpl])
    bind(classOf[TwitterAdapter]).to(classOf[TwitterAdapterImpl])
  }
}
