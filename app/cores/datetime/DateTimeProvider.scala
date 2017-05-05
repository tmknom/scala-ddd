package cores.datetime

import java.time.{Clock, ZoneId, ZonedDateTime}

/**
  * 現在日時を取得するクラス
  *
  * プロダクトコードでは何も考えず nowJST メソッドを呼び出すだけでOK。
  * テストコードでは FixedDateTimeProvider クラスで現在日時を固定し、テストを行う。
  *
  * 現在日時の取得は必ず本クラスを介して行うこと。
  * 標準の「ZonedDateTime.now()」などを使うと、テスト時に現在時刻を固定できなくなるので注意。
  *
  * その他、本クラス導入の経緯については下記に記述している。
  * https://crowdworks.qiita.com/tmknom/items/e5283726dafc106307ad
  */
object DateTimeProvider {
  /**
    * タイムゾーン：JST
    *
    * JSTはよく使うので、定数定義しておく
    */
  val JST: ZoneId = ZoneId.of("Asia/Tokyo")

  /**
    * 現在の時点の日時へのアクセスを提供するクロック
    *
    * テストクラス一個だとvolatileなしでも問題ないが、複数テスト走らせるとvolatileなしだとテストこける可能性がある。（過去の経験上）
    * joda-timeを参照したトコロ、同様の機構にvolatileをつけて実装しているので、同じように実装している。
    * 参考：https://github.com/JodaOrg/joda-time/blob/master/src/main/java/org/joda/time/DateTimeUtils.java
    *
    * なお、Wartremoverにvarを使うなと怒られるが、ここではvarにしないといけない箇所なので、警告は抑制する。
    */
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  @volatile
  private var clock: Clock = Clock.systemDefaultZone()

  /**
    * JSTの現在日時の取得
    *
    * @return 指定したタイムゾーンの現在日時
    */
  def nowJST(): ZonedDateTime = {
    now(JST)
  }

  /**
    * 指定したタイムゾーンの現在日時の取得
    *
    * @param zoneId タイムゾーンID
    * @return 指定したタイムゾーンの現在日時
    */
  private[this] def now(zoneId: ZoneId): ZonedDateTime = {
    ZonedDateTime.ofInstant(clock.instant(), zoneId)
  }
}

/**
  * DateTimeProvider のコンパニオンクラス
  *
  * シングルトンで定義された DateTimeProvider の private 変数 Clock にアクセスするために定義している。
  *
  * また、アクセス修飾子を protected[this] とすることで、本クラスを継承したクラス以外からメソッドを呼び出すのは不可能。
  * そして、テストコード側のみに、本クラスを継承するクラスを定義することで、プロダクトコードからのアクセスを防止する。
  */
protected[this] class DateTimeProvider {
  /**
    * クロックのセッター
    *
    * テストコード側で継承したクラスから呼び出されることを想定。
    * protected[this] とすることで、プロダクトコード側で不用意にセッターにアクセスすることを抑制している。
    *
    * DateTimeProvider を継承したクラスを定義すれば、プロダクトコード側でもアクセス可能だが、
    * 当然そんなことはしてはいけない。
    *
    * @param clock クロック
    */
  protected[this] def setClockForTest(clock: Clock): Unit = {
    DateTimeProvider.clock = clock
  }
}
