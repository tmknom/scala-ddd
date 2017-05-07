package infrastructures.tweet

import java.util.Date

import domains.tweet.TwitterApi
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import resources.builder.DateTimeBuilder
import twitter4j._

@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
class TweetApiImplSpec extends PlaySpec with MockitoSugar {

  val sut: TwitterApi = Application.instanceCache[TwitterApi].apply(MockBuilder.build())

  "request" should {
    "success" in {
      val actual = sut.request()
      actual.length mustBe 1
    }
  }

  private object MockBuilder {
    def build(): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[TwitterAdapter].toInstance(mockTwitterAdapter())).
        build
    }

    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    def mockTwitterAdapter(): TwitterAdapter = {
      // Mockが返す値を作成
      val value = Seq[Status](new DummyStatus)


      // Mockが返す値をセット
      val mockInstance = mock[TwitterAdapter]
      when(mockInstance.search(any[Twitter], any[Query])).thenReturn(value)
      mockInstance
    }
  }

  // scalastyle:off
  private class DummyStatus extends Status {
    def getCreatedAt: Date = Date.from(DateTimeBuilder.fixedJST.toInstant)

    def getId: Long = 0

    def getText: String = ""

    def getSource: String = ""

    def isTruncated: Boolean = true

    def getInReplyToStatusId: Long = 0

    def getInReplyToUserId: Long = 0

    def getInReplyToScreenName: String = ""

    def getGeoLocation: GeoLocation = null

    def getPlace: Place = null

    def isFavorited: Boolean = true

    def isRetweeted: Boolean = true

    def getFavoriteCount: Int = 0

    def getUser: User = new DummyUser

    def isRetweet: Boolean = true

    def getRetweetedStatus: Status = null

    def getContributors: Array[Long] = Array.empty[Long]

    def getRetweetCount: Int = 0

    def isRetweetedByMe: Boolean = true

    def getCurrentUserRetweetId: Long = 0

    def isPossiblySensitive: Boolean = true

    def getLang: String = ""

    def getScopes: Scopes = null

    def getWithheldInCountries: Array[String] = Array.empty[String]

    def getQuotedStatusId: Long = 0

    def getQuotedStatus: Status = null

    def getRateLimitStatus: RateLimitStatus = null

    def getAccessLevel: Int = 0

    def compareTo(o: Status) = 0

    def getUserMentionEntities: Array[UserMentionEntity] = Array.empty[UserMentionEntity]

    def getURLEntities: Array[URLEntity] = Array.empty[URLEntity]

    def getHashtagEntities: Array[HashtagEntity] = Array.empty[HashtagEntity]

    def getMediaEntities: Array[MediaEntity] = Array.empty[MediaEntity]

    def getExtendedMediaEntities: Array[ExtendedMediaEntity] = Array.empty[ExtendedMediaEntity]

    def getSymbolEntities: Array[SymbolEntity] = Array.empty[SymbolEntity]
  }

  private class DummyUser extends User {
    def getId: Long = 0

    def getName: String = ""

    def getScreenName: String = ""

    def getLocation: String = ""

    def getDescription: String = ""

    def isContributorsEnabled: Boolean = true

    def getProfileImageURL: String = ""

    def getBiggerProfileImageURL: String = ""

    def getMiniProfileImageURL: String = ""

    def getOriginalProfileImageURL: String = ""

    def getProfileImageURLHttps: String = ""

    def getBiggerProfileImageURLHttps: String = ""

    def getMiniProfileImageURLHttps: String = ""

    def getOriginalProfileImageURLHttps: String = ""

    def isDefaultProfileImage: Boolean = true

    def getURL: String = ""

    def isProtected: Boolean = true

    def getFollowersCount: Int = 0

    def getStatus: Status = null

    def getProfileBackgroundColor: String = ""

    def getProfileTextColor: String = ""

    def getProfileLinkColor: String = ""

    def getProfileSidebarFillColor: String = ""

    def getProfileSidebarBorderColor: String = ""

    def isProfileUseBackgroundImage: Boolean = true

    def isDefaultProfile: Boolean = true

    def isShowAllInlineMedia: Boolean = true

    def getFriendsCount: Int = 0

    def getCreatedAt: Date = null

    def getFavouritesCount: Int = 0

    def getUtcOffset: Int = 0

    def getTimeZone: String = ""

    def getProfileBackgroundImageURL: String = ""

    def getProfileBackgroundImageUrlHttps: String = ""

    def getProfileBannerURL: String = ""

    def getProfileBannerRetinaURL: String = ""

    def getProfileBannerIPadURL: String = ""

    def getProfileBannerIPadRetinaURL: String = ""

    def getProfileBannerMobileURL: String = ""

    def getProfileBannerMobileRetinaURL: String = ""

    def isProfileBackgroundTiled: Boolean = true

    def getLang: String = ""

    def getStatusesCount: Int = 0

    def isGeoEnabled: Boolean = true

    def isVerified: Boolean = true

    def isTranslator: Boolean = true

    def getListedCount: Int = 0

    def isFollowRequestSent: Boolean = true

    def getDescriptionURLEntities: Array[URLEntity] = Array.empty[URLEntity]

    def getURLEntity: URLEntity = null

    def getWithheldInCountries: Array[String] = Array.empty[String]

    def compareTo(o: User) = 0

    def getRateLimitStatus: RateLimitStatus = null

    def getAccessLevel: Int = 0
  }

  // scalastyle:on
}
