import java.time.Clock

import com.google.inject.AbstractModule
import domains.article.ArticleRepository
import domains.crawler._
import infrastructures.api.crawler.HatenaBookmarkApiImpl
import infrastructures.parser.crawler.HatenaBookmarkParserImpl
import infrastructures.repositories.article.ArticleRepositoryImpl
import services.article._
import services.crawler._
import services.{ApplicationTimer, AtomicCounter, Counter}

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])

    // とりあえずレイヤ単位でメソッドを切っておく
    configureServices()
    // infrastructures はテスト時にモックにしたいケースが多いので、さらに細かくメソッドを切っておく
    configureRepositories()
    configureApi()
    configureParser()
  }

  private def configureServices() = {
    bind(classOf[HatenaBookmarkService]).to(classOf[HatenaBookmarkServiceImpl])
    bind(classOf[ArticleService]).to(classOf[ArticleServiceImpl])
  }

  private def configureRepositories() = {
    bind(classOf[ArticleRepository]).to(classOf[ArticleRepositoryImpl])
  }

  private def configureApi() = {
    bind(classOf[HatenaBookmarkApi]).to(classOf[HatenaBookmarkApiImpl])
  }

  private def configureParser() = {
    bind(classOf[HatenaBookmarkParser]).to(classOf[HatenaBookmarkParserImpl])
  }
}
