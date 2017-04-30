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
scalaVersion := "2.11.11"

// リソースファイルのパスの設定
// デフォルトでは、src/test/resourcesが設定されているが変更する
// http://www.scala-sbt.org/0.13/docs/Howto-Customizing-Paths.html#Change+the+default+resource+directory
//
// テスト用のFixtureファイルをロードできるように設定
resourceDirectory in Test := baseDirectory.value / "test/resources"

// テスト時に読み込むlogbackの設定を切り替え
javaOptions in Test += "-Dlogger.resource=logback-test.xml"

// 依存ライブラリ
// 定期的にバージョンアップしたい
libraryDependencies ++= Seq(
  // DB接続用でおなじみ
  jdbc,
  // DBマイグレーション
  evolutions,
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
  "mysql" % "mysql-connector-java" % "5.1.41",
  // O/Rマッパー
  // http://skinny-framework.org/documentation/orm.html
  "org.skinny-framework" %% "skinny-orm" % "2.3.6",
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
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.1",
  // play-cache ってのインポートしてるっぽい
  // キャッシュ絡みのなにかということしか分からない（何のキャッシュだよ。。）
  cache,
  // websocketライブラリ
  // 我々にはいらないのでは疑惑がある
  ws,
  // HTTP通信用ライブラリ
  // wsよりコッチを使うほうが推奨されているっぽい
  // http://qiita.com/bigwheel/items/44cb874ced4be204c09c
  "net.databinder.dispatch" %% "dispatch-core" % "0.12.0",

  // ちゃんと調べてないけど、こっから下はテスト用だと思われる
  //
  // モック用ライブラリ
  // DBアクセスやネットワークアクセスを差し替えたい時に使う
  "org.mockito" % "mockito-core" % "2.7.22" % Test,
  // xUnit用ライブラリ
  // playの標準テストライブラリなので、そのまま採用する
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
)

// コンパイル時にドキュメントを含めない
// こうすると、コンパイルがちょっと高速になるかもしれない
// https://www.playframework.com/documentation/2.5.x/SBTCookbook#Disable-documentation
sources in(Compile, doc) := Seq.empty

// Artifact作成時にドキュメントを含めない
// こうすると、Artifact作成がちょっと高速になるかもしれない
//
// http://www.scala-sbt.org/0.13/docs/Artifacts.html
// disable publishing the main jar produced by `package`
publishArtifact in(Compile, packageBin) := false
// disable publishing the main API jar
publishArtifact in(Compile, packageDoc) := false
// disable publishing the main sources jar
publishArtifact in(Compile, packageSrc) := false

/**
  * カバレッジの除外対象
  *
  * ドキュメントにはこれで除外できそうな記述がある。が、動いてくれない。。
  * @see https://github.com/scoverage/sbt-scoverage#exclude-classes-and-packages
  */
// coverageExcludedPackages := "controllers.Reverse.*;controllers.javascript.Reverse.*"

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
  * WartRemover のプロダクトコード側の設定
  *
  * - 警告の除外設定
  *   - Overloading : オーバーロードは普通に使われるものであるため除外
  * - 警告の除外ファイル
  *   - routes : ルーティング設定は事実上のDSLなのでチェックしない
  *
  * @see http://www.wartremover.org/doc/install-setup.html
  */
wartremoverErrors in(Compile, compile) ++= Warts.allBut(Wart.Overloading)
// 本当は右記のように書こうとした => wartremoverExcluded += baseDirectory.value / "conf" / "routes"
// が、除外してくれなかったので、このような書き方に落ち着いた。たぶん、conf配下のファイルは扱いが特殊なんだろう。
// http://stackoverflow.com/questions/34788530/wartremover-still-reports-warts-in-excluded-play-routes-file
wartremoverExcluded ++= routes.in(Compile).value

/**
  * WartRemover のテストコード側の設定
  *
  * - 警告の除外設定
  *   - Overloading : オーバーロードは許可しておかないと、テストのbeforeEachとか怒られるので除外
  *   - OptionPartial : コントローラでよく出てくるイディオム route(app, FakeRequest(GET, "/any/url")).get が引っかかるため除外
  *
  * - OptionPartial を抑制する詳細な理由
  *   - 抑制しない場合、コントローラのテストなどで「Option#get is disabled - use Option#fold instead」という警告が出る。
  *   - プロダクトコードでは確かに、Option 型で get メソッドを使わず、fold メソッドを使うというのは良い習慣に思える。
  *   - 一方で、コントローラのテストでは route(app, FakeRequest(GET, "/any/url")).get というイディオムがよく出てくる。
  *   - コントローラのテストでは、fold メソッドなどを使うと逆にテストの見通しが悪くなるように見える。
  *   - よって、テストコードでは OptionPartial を抑制することにした。
  *   - 本当はコントローラのテストコードだけ指定とかできればよいが、そこは妥協することとした。
  */
wartremoverErrors in(Test, test) ++= Warts.allBut(Wart.Overloading, Wart.OptionPartial)

/**
  * sbt-updates の依存ライブラリアップデートチェックの対象外を設定
  *
  * dependencyUpdatesExclusions は非推奨になったから代わりに
  * dependencyUpdatesFilter を使えって書いてあるけど、なぜか使えない。。
  *
  * @see https://github.com/rtimush/sbt-updates#exclusions
  */
dependencyUpdatesExclusions := moduleFilter(organization = "org.scala-lang")

/**
  * sbt-updates の依存ライブラリアップデートチェック時に、アップデートすべきものがあればエラーにする
  */
dependencyUpdatesFailBuild := true
