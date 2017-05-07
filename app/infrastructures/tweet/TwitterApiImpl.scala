package infrastructures.tweet

import java.time.{ZoneId, ZonedDateTime}
import javax.inject.{Inject, Singleton}

import com.typesafe.config.ConfigFactory
import domains.tweet.{TweetEntity, TwitterApi}
import twitter4j.conf.{Configuration, ConfigurationBuilder}
import twitter4j.{Query, Status, Twitter, TwitterFactory}

import scala.collection.JavaConverters._

@Singleton
class TwitterApiImpl @Inject()(twitterAdapter: TwitterAdapter) extends TwitterApi {
  override def request(): Seq[TweetEntity] = {
    val query = new Query("#funny")
    val statuses = search(query)
    statuses.map(TweetEntityConverter.convert)
  }

  private def search(query: Query): Seq[Status] = {
    val twitter = TwitterClientFactory.create()
    twitterAdapter.search(twitter, query)
  }
}

trait TwitterAdapter {
  def search(twitter: Twitter, query: Query): Seq[Status]
}

@Singleton
class TwitterAdapterImpl extends TwitterAdapter {
  def search(twitter: Twitter, query: Query): Seq[Status] = twitter.search(query).getTweets.asScala
}

private[tweet] object TweetEntityConverter {
  def convert(status: Status): TweetEntity = {
    TweetEntity(
      None,
      status.getId,
      status.getUser.getScreenName,
      status.getText,
      status.getRetweetCount,
      status.getFavoriteCount,
      status.getLang,
      ZonedDateTime.ofInstant(status.getCreatedAt.toInstant, ZoneId.systemDefault())
    )
  }
}

private[tweet] object TwitterClientFactory {
  def create(): Twitter = {
    new TwitterFactory(configuration()).getInstance()
  }

  private def configuration(): Configuration = {
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
}
