/**
  * 依存ライブラリの定義
  */

import play.sbt.PlayImport.{evolutions, jdbc}
import sbt._

object Versions {
  // DB関連
  val MysqlConnectorJava = "5.1.42"
  val SkinnyOrm = "2.3.7"
  val ScalikejdbcPlayInitializer = "2.5.1"
  val ScalikejdbcJsr310 = "2.5.2"

  // 通信関連
  val DispatchCore = "0.12.0"
  val Twitter4jCore = "4.0.6"

  // JSON関連
  val SprayJson = "1.3.3"

  // テスト関連
  val MockitoCore = "2.8.9"
  val ScalatestplusPlay = "2.0.0"
}

// noinspection TypeAnnotation
// IntelliJの警告が鬱陶しいので抑制
object Libraries {
  // MySQLで接続するのに必要だぞ！
  // 一度6.0.6にバージョンを上げてみたが、警告が出るようになったため、5系の最新版にしておく
  //
  // play が com.mysql.jdbc.Driver を使っているためこのような警告が出ると推測されるが
  // 今後 play がバージョンアップを重ねていけば、そのうち com.mysql.cj.jdbc.Driver になる気がする
  //
  // jdbc については、一応 play 推奨のバージョンでいきたいので、一旦5系のままでいくことにする
  //
  // scalastyle:off
  // 6.0.6のときに出力された警告
  //   Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
  //   [warn] c.z.h.u.DriverDataSource - Registered driver with driverClassName=com.mysql.jdbc.Driver was not found, trying direct instantiation.
  // scalastyle:on
  val MysqlConnectorJava = "mysql" % "mysql-connector-java" % Versions.MysqlConnectorJava

  // O/Rマッパー
  // http://skinny-framework.org/documentation/orm.html
  val SkinnyOrm = "org.skinny-framework" %% "skinny-orm" % Versions.SkinnyOrm

  // コネクションプールの作成に必要
  //
  // Skinny-ORM は内部的に ScalikeJDBC を使っており、コネクションプールを初期化する必要がある
  // そのコネクションプールの初期化を担ってくれるようだ
  //
  // なお application.conf に下記記述が必要
  // play.modules.enabled += "scalikejdbc.PlayModule"
  //
  // 余談だが、ScalikeJDBC 自体は日本人が開発しているようで、日本語ドキュメントが開発者によって公開されている
  // https://github.com/scalikejdbc/scalikejdbc-cookbook/tree/master/ja
  //
  // また ScalikeJDBC は近々メジャーバージョンアップが予定されているもよう
  // サポート対象をJava8以上のみとして、JSR-310（ZonedDateTimeとか）にデフォルトで対応するらしい
  // よく分からんけど Reactive Streams というのにも標準対応するらしい
  // https://github.com/scalikejdbc/scalikejdbc/blob/master/notes/3.0.0.markdown
  val ScalikejdbcPlayInitializer = "org.scalikejdbc" %% "scalikejdbc-play-initializer" % Versions.ScalikejdbcPlayInitializer

  // scalikejdbc で ZonedDateTime を使うためのライブラリ
  // https://github.com/scalikejdbc/scalikejdbc-cookbook/blob/master/ja/06_samples.md#joda-time-ではなく-java-se-8-の-date-time-api-を使う
  val ScalikejdbcJsr310 = "org.scalikejdbc" %% "scalikejdbc-jsr310" % Versions.ScalikejdbcJsr310

  // HTTP通信用ライブラリ
  // wsよりコッチを使うほうが推奨されているっぽい
  // http://qiita.com/bigwheel/items/44cb874ced4be204c09c
  val DispatchCore = "net.databinder.dispatch" %% "dispatch-core" % Versions.DispatchCore

  // Twitter API Client
  // Java で最もメジャーなライブラリっぽい
  // https://github.com/yusuke/twitter4j
  val Twitter4jCore = "org.twitter4j" % "twitter4j-core" % Versions.Twitter4jCore

  // JSON変換用ライブラリ
  // Scala オブジェクトから JSON にいい感じに変換してくれる
  // http://arata.hatenadiary.com/entry/2015/02/11/015916
  val SprayJson = "io.spray" %% "spray-json" % Versions.SprayJson

  // モック用ライブラリ
  // DBアクセスやネットワークアクセスを差し替えたい時に使う
  val MockitoCore = "org.mockito" % "mockito-core" % Versions.MockitoCore % Test

  // xUnit用ライブラリ
  // playの標準テストライブラリなので、そのまま採用する
  val ScalatestplusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.ScalatestplusPlay % Test
}

// noinspection TypeAnnotation
object Dependencies {

  import Libraries._

  // 最低限必要なライブラリ
  val Base = Seq(
    jdbc,
    evolutions,
    MysqlConnectorJava,
    SkinnyOrm,
    ScalikejdbcPlayInitializer,
    ScalikejdbcJsr310,
    DispatchCore,
    SprayJson
  )

  // テスト関連
  val Test = Seq(
    MockitoCore,
    ScalatestplusPlay
  )

  // アプリケーションの依存関係
  val Application = Base ++ Test ++ Seq(
    Twitter4jCore
  )
}
