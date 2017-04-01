// アプリケーションの名前
// リポジトリ名と合わせとくのが混乱がなくて良いと思われ
name := """scala-ddd"""

// アプリケーションのバージョン
// 普段は書き換えることなさそう
version := "1.0-SNAPSHOT"

// ビルド定義というらしい
// サブプロジェクトとか作るときなどに色々弄るっぽい
// あまり知見がないので、最初は activator さんが吐き出したまま使おう
lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Scalaのバージョン
// たまにはアップデートしてあげたい
scalaVersion := "2.11.7"

// リソースファイルのパスの設定
// デフォルトでは、src/test/resourcesが設定されているが変更する
// http://www.scala-sbt.org/0.13/docs/Howto-Customizing-Paths.html#Change+the+default+resource+directory
//
// テスト用のFixtureファイルをロードできるように設定
resourceDirectory in Test := baseDirectory.value / "test/fixtures"

// 依存ライブラリ
// 定期的にバージョンアップしたい
libraryDependencies ++= Seq(
  // DB接続用でおなじみ
  // どうやらslickを使うときは消さないといけないっぽい
  // https://playframework.com/documentation/ja/2.4.x/PlaySlickFAQ
  // jdbc,
  // MySQLで接続するのに必要だぞ！
  // なんか6.0.X系もあるけど、どっちがいいんだろう
  "mysql" % "mysql-connector-java" % "5.1.41",
  // O/Rマッパー
  // https://github.com/playframework/play-slick
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  // DBマイグレーション
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  // play-cache ってのインポートしてるっぽい
  // キャッシュ絡みのなにかということしか分からない（何のキャッシュだよ。。）
  cache,
  // websocketライブラリ
  // 我々にはいらないのでは疑惑がある
  ws,
  // HTTP通信用ライブラリ
  // wsよりコッチを使うほうが推奨されているっぽい
  // http://qiita.com/bigwheel/items/44cb874ced4be204c09c
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",

  // ちゃんと調べてないけど、こっから下はテスト用だと思われる
  //
  // モック用ライブラリ
  // DBアクセスやネットワークアクセスを差し替えたい時に使う
  "org.mockito" % "mockito-core" % "2.7.20" % Test,
  // xUnit用ライブラリ
  // playの標準テストライブラリなので、そのまま採用する
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

