package cores.datetime

import java.time._

object CurrentFactory {
  /**
    * 現在の時点の日時へのアクセスを提供するクロック
    *
    * テストクラス一個だとvolatileなしでも問題ないが、複数テスト走らせるとvolatileなしだとテストこける可能性がある（過去の経験上）
    * joda-timeを参照したトコロ、同様の機構にvolatileをつけて実装しているので、同じように実装している
    * 参考：https://github.com/JodaOrg/joda-time/blob/master/src/main/java/org/joda/time/DateTimeUtils.java
    */
  @volatile
  private var clock: Clock = Clock.systemDefaultZone()

  /**
    * 現在日時を生成する
    *
    * @return 現在日時
    */
  def factoryLocalDateTime(): LocalDateTime = {
    LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault())
  }

  /**
    * 指定したタイムゾーンの現在日時を生成する
    *
    * @param zoneId タイムゾーンID
    * @return 指定したタイムゾーンの現在日時
    */
  def factoryZonedDateTime(zoneId: ZoneId): ZonedDateTime = {
    ZonedDateTime.ofInstant(clock.instant(), zoneId)
  }

  /**
    * 現在時刻を生成する
    *
    * @return 現在時刻
    */
  def factoryLocalTime(): LocalTime = {
    LocalTime.from(factoryLocalDateTime())
  }

  /**
    * 現在日付を生成する
    *
    * @return 現在日付
    */
  def factoryLocalDate(): LocalDate = {
    LocalDate.from(factoryLocalDateTime())
  }

  /**
    * テスト用のクロックをセット
    *
    * テストコードからのみ呼び出されることを想定している。
    */
  def setForTest(clock: Clock): Unit = {
    this.clock = clock
  }

  /**
    * システムクロックにリセット
    *
    * テストコードからのみ呼び出されることを想定している。
    */
  def resetForTest(): Unit = {
    clock = Clock.systemDefaultZone()
  }
}
