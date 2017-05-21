package cores.datetime

import java.time.{Clock, LocalDateTime, ZoneId, ZonedDateTime}

/**
  * 現在日時を固定するクラス
  *
  * テスト時に、現在日時を固定できる仕組みを提供する。
  *
  * テストコードでは、テスト開始前に fix メソッドを呼び出して現在日時を固定し、
  * テスト終了後に reset メソッドを呼び出して、元に戻す。
  *
  * 現在日時に関する、設計は下記も参考にしている。
  * http://stackoverflow.com/questions/24491260/mocking-time-in-java-8s-java-time-api
  */
object FixedDateTimeProvider {
  private[this] val clockHolder: ClockHolder = new ClockHolder()

  /**
    * 現在日時を固定したクロックを使用するよう設定
    *
    * @param localDateTime 固定する現在日時
    * @param zoneId        タイムゾーンID
    */
  def fix(localDateTime: LocalDateTime, zoneId: ZoneId): Unit = {
    clockHolder.set(Clock.fixed(ZonedDateTime.of(localDateTime, zoneId).toInstant, zoneId))
  }

  /**
    * システムクロックを使用するよう設定
    */
  def reset(): Unit = {
    clockHolder.set(Clock.systemDefaultZone())
  }
}

/**
  * クロックを保持するクラス
  */
private[datetime] class ClockHolder extends DateTimeProvider {
  /**
    * クロックのセッターのラッパーメソッド
    *
    * プロダクトコードで定義しているメソッドのアクセスの制限がキツイため
    * そのまま FixedDateTimeProvider からセッターにアクセスできない。
    *
    * そこで、ラッパーメソッドを定義することで、セッターへのアクセスを実現する。
    *
    * @param clock クロック
    */
  private[datetime] def set(clock: Clock): Unit = {
    setClockForTest(clock)
  }
}
