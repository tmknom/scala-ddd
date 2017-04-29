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

// コンパイル時にドキュメントを含めない
// こうすると、コンパイルがちょっと高速になるかもしれない
// https://www.playframework.com/documentation/2.5.x/SBTCookbook#Disable-documentation
sources in (Compile, doc) := Seq.empty

// Artifact作成時にドキュメントを含めない
// こうすると、Artifact作成がちょっと高速になるかもしれない
//
// http://www.scala-sbt.org/0.13/docs/Artifacts.html
// disable publishing the main jar produced by `package`
publishArtifact in (Compile, packageBin) := false
// disable publishing the main API jar
publishArtifact in (Compile, packageDoc) := false
// disable publishing the main sources jar
publishArtifact in (Compile, packageSrc) := false

/**
  * Scalastyleでテストコード側もデフォルトでチェックするよう設定
  *
  * テストコードもプロダクトコード同様の品質にすべきである。
  *
  * 標準ではテストコードをチェックする場合 activator test:scalastyle を叩くことになるが、
  * この設定を入れることで activator scalastyle を叩いたときも、テストコードをチェックしてくれる。
  */
scalastyleSources in Compile <++= sourceDirectories in Test

/**
  * テスト時もついでに Scalastyle を実行するよう設定
  *
  * activator scalastyle コマンドを実行した場合は compile 用の設定が読み込まれる。
  * 同様の設定をテスト時のチェックにも使いまわしたいのでCompileを指定している。
  */
lazy val testScalastyle = taskKey[Unit]("testScalastyle")
testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(test in Test) <<= (test in Test) dependsOn testScalastyle

/**
  * WartRemover の設定
  *
  * - 警告の除外設定
  *   - Overloading : オーバーロードは許可しておかないと、テストのbeforeEachとか怒られるので除外
  * - 警告の除外ファイル
  *   - routes : ルーティング設定は事実上のDSLなのでチェックしない
  *
  * @see http://www.wartremover.org/doc/install-setup.html
  */
wartremoverWarnings ++= Warts.allBut(Wart.Overloading)
// 本当は右記のように書こうとした => wartremoverExcluded += baseDirectory.value / "conf" / "routes"
// が、除外してくれなかったので、このような書き方に落ち着いた。たぶん、conf配下のファイルは扱いが特殊なんだろう。
// http://stackoverflow.com/questions/34788530/wartremover-still-reports-warts-in-excluded-play-routes-file
wartremoverExcluded ++= routes.in(Compile).value
