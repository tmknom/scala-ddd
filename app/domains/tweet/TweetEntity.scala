package domains.tweet

import java.time.ZonedDateTime

import cores.spray.ZonedDateTimeImplicits._
import spray.json._

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
final case class TweetEntity(
                              id: Option[Int] = None,
                              originId: Long,
                              userScreenName: String,
                              text: String,
                              retweetCount: Int,
                              favoriteCount: Int,
                              lang: String,
                              createdDateTime: ZonedDateTime
                            ) {
}

object TweetEntityJsonProtocol extends DefaultJsonProtocol {
  implicit val format: RootJsonFormat[TweetEntity] = jsonFormat8(TweetEntity)
}
