package infrastructures.tweet

import domains.tweet.{TweetEntity, TweetRepository}
import scalikejdbc._
import scalikejdbc.jsr310._
import skinny.orm.{Alias, SkinnyCRUDMapper}

class TweetRepositoryImpl extends SkinnyCRUDMapper[TweetEntity] with TweetRepository {
  override lazy val defaultAlias: Alias[TweetEntity] = createAlias("a")
  override lazy val tableName: String = "tweets"

  override def extract(rs: WrappedResultSet, rn: ResultName[TweetEntity]): TweetEntity = TweetEntity(
    id = Some(rs.int(rn.id)),
    originId = rs.long(rn.originId),
    userScreenName = rs.string(rn.userScreenName),
    text = rs.string(rn.text),
    retweetCount = rs.int(rn.retweetCount),
    favoriteCount = rs.int(rn.favoriteCount),
    lang = rs.string(rn.lang),
    createdDateTime = rs.zonedDateTime(rn.createdDateTime)
  )

  override def listAll(): Seq[TweetEntity] = findAll()

  override def insert(tweetEntity: TweetEntity): Long = createWithNamedValues(
    column.originId -> tweetEntity.originId,
    column.userScreenName -> tweetEntity.userScreenName,
    column.text -> tweetEntity.text,
    column.retweetCount -> tweetEntity.retweetCount,
    column.favoriteCount -> tweetEntity.favoriteCount,
    column.lang -> tweetEntity.lang,
    column.createdDateTime -> tweetEntity.createdDateTime
  )
}
