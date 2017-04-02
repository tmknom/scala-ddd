import java.time.Clock

import com.google.inject.AbstractModule
import domains.article._
import domains.crawler._
import infrastructures.article._
import infrastructures.crawler._
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
    configureInfrastructures()
  }

  private def configureServices() = {
    bind(classOf[HatenaBookmarkService]).to(classOf[HatenaBookmarkServiceImpl])
    bind(classOf[ArticleService]).to(classOf[ArticleServiceImpl])
  }

  private def configureInfrastructures() = {
    bind(classOf[ArticleRepository]).to(classOf[ArticleRepositoryImpl])
    bind(classOf[HatenaBookmarkApi]).to(classOf[HatenaBookmarkApiImpl])
    bind(classOf[HatenaBookmarkParser]).to(classOf[HatenaBookmarkParserImpl])
  }
}
