package cores.datetime

import java.time.{Clock, LocalDateTime, ZoneId, ZonedDateTime}

/**
  * 現在日時を取得するクラス
  *
  * テスト時に、現在日時を固定できる仕組みを提供する。
  * プロダクトコードでは、「DateTimeProvider.now()」と書けばOK。
  *
  * テストコードでは、テスト開始前に useFixedClockForTest メソッドを呼び出して現在日時を固定し、
  * テスト終了後に useFixedClockForTest メソッドを呼び出して、元に戻す。
  *
  * 標準の「LocalDateTime.now()」などを使うと、テスト時に現在時刻を固定できなくなるので注意。
  *
  * 現在日時に関する、議論は下記も参考になる。
  * http://stackoverflow.com/questions/24491260/mocking-time-in-java-8s-java-time-api
  */
object DateTimeProvider {

  /**
    * 現在の時点の日時へのアクセスを提供するクロック
    *
    * テストクラス一個だとvolatileなしでも問題ないが、複数テスト走らせるとvolatileなしだとテストこける可能性がある（過去の経験上）
    * joda-timeを参照したトコロ、同様の機構にvolatileをつけて実装しているので、同じように実装している
    * 参考：https://github.com/JodaOrg/joda-time/blob/master/src/main/java/org/joda/time/DateTimeUtils.java
    *
    * なお、Wartremoverにvarを使うなと怒られるが、ここではvarにしないといけない箇所なので、警告は抑制する。
    */
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  @volatile
  private var clock: Clock = Clock.systemDefaultZone()

  /**
    * システムのデフォルトタイームゾーンID
    */
  private val DEFAULT_ZONE_ID: ZoneId = ZoneId.systemDefault()

  /**
    * 現在日時の取得
    *
    * @return 現在日時
    */
  def now(): LocalDateTime = {
    now(DEFAULT_ZONE_ID).toLocalDateTime
  }

  /**
    * 指定したタイムゾーンの現在日時の取得
    *
    * @return 指定したタイムゾーンの現在日時
    */
  def now(zoneId: ZoneId): ZonedDateTime = {
    ZonedDateTime.ofInstant(clock.instant(), zoneId)
  }

  /**
    * テスト用のクロックを使用
    *
    * テストコードからのみ呼び出されることを想定している。
    * システムのデフォルトタイムゾーンを使用する。
    *
    * @param localDateTime 固定する現在日時
    */
  def useFixedClockForTest(localDateTime: LocalDateTime): Unit = {
    useFixedClockForTest(localDateTime, DEFAULT_ZONE_ID)
  }

  /**
    * テスト用のクロックを使用
    *
    * テストコードからのみ呼び出されることを想定している。
    *
    * @param localDateTime 固定する現在日時
    * @param zoneId タイムゾーンID（省略時はシステムのデフォルトタイムゾーンを使用）
    */
  def useFixedClockForTest(localDateTime: LocalDateTime, zoneId: ZoneId): Unit = {
    clock = Clock.fixed(ZonedDateTime.of(localDateTime, zoneId).toInstant, zoneId)
  }

  /**
    * システムのクロックを使用
    *
    * テストコードからのみ呼び出されることを想定している。
    */
  def useSystemClockForTest(): Unit = {
    clock = Clock.systemDefaultZone()
  }
}
