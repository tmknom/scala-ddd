package infrastructures.crawler

import javax.inject.Singleton

import com.typesafe.config.ConfigFactory
import domains.crawler.TwitterApi
import play.api.Logger
import twitter4j.conf.{Configuration, ConfigurationBuilder}
import twitter4j.{Query, Status, TwitterFactory}

import scala.collection.JavaConverters._

@SuppressWarnings(Array("org.wartremover.warts.ToString", "org.wartremover.warts.TraversableOps"))
@Singleton
class TwitterApiImpl extends TwitterApi {
  override def request(): String = {
    val query = new Query("#funny")
    val statuses = search(query)

    val status = statuses.head
    logStatus(status)
    status.toString
  }

  private def search(query: Query): List[Status] = {
    twitterClient(configuration).search(query).getTweets.asScala.toList
  }

  private def twitterClient(configuration: Configuration) = {
    new TwitterFactory(configuration).getInstance()
  }

  private def configuration: Configuration = {
    val config = ConfigFactory.load().getObject("twitter4j").toConfig
    val oauthConfig = config.getObject("oauth").toConfig

    val builder = new ConfigurationBuilder
    builder.setDebugEnabled(config.getBoolean("debug"))
      .setOAuthConsumerKey(oauthConfig.getString("consumerKey"))
      .setOAuthConsumerSecret(oauthConfig.getString("consumerSecret"))
      .setOAuthAccessToken(oauthConfig.getString("accessToken"))
      .setOAuthAccessTokenSecret(oauthConfig.getString("accessTokenSecret"))
      .build()
  }

  private def logStatus(status: Status): Unit = {
    Logger.warn(s"getId=${status.getId}")
    Logger.warn(s"getText=${status.getText}")
    Logger.warn(s"getCreatedAt=${status.getCreatedAt}")
    Logger.warn(s"getRetweetCount=${status.getRetweetCount}")
    Logger.warn(s"getFavoriteCount=${status.getFavoriteCount}")
    Logger.warn(s"getUser.getScreenName=${status.getUser.getScreenName}")
//    Logger.warn(s"isRetweeted=${status.isRetweeted}")
//    Logger.warn(s"getMediaEntities=${status.getMediaEntities.head.toString}")
//    Logger.warn(s"getSource=${status.getUserMentionEntities.head.toString}")
//    Logger.warn(s"getSource=${status.getHashtagEntities.head.toString}")
//    Logger.warn(s"getUser=${status.getUser.toString}")
//    Logger.warn(s"getMediaEntities.length=${status.getMediaEntities.length}")
//    Logger.warn(s"getUserMentionEntities.length=${status.getUserMentionEntities.length}")
//    Logger.warn(s"getMediaEntities.length=${status.getMediaEntities.length}")
  }
}
