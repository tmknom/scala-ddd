// アプリケーションの名前
// リポジトリ名と合わせとくのが混乱がなくて良いと思われ
name := """scala-ddd"""

// アプリケーションのバージョン
// 普段は書き換えることなさそう
version := "1.0-SNAPSHOT"

// ビルド定義というらしい
// サブプロジェクトとか作るときなどに色々弄るっぽい
lazy val root = (project in file("."))
  .settings(Coverage.Settings)
  .enablePlugins(PlayScala)
  .enablePlugins(CopyPasteDetector)

// Scalaのバージョン
// たまにはアップデートしてあげたい
scalaVersion := "2.11.11"

// リソースファイルのパスの設定
// デフォルトでは、src/test/resourcesが設定されているが変更する
// http://www.scala-sbt.org/0.13/docs/Howto-Customizing-Paths.html#Change+the+default+resource+directory
//
// テスト用のFixtureファイルをロードできるように設定
resourceDirectory in Test := baseDirectory.value / "test/resources"

/**
  * システムのタイムゾーンを指定
  *
  * scalikejdbc などの一部のライブラリでは、システムのタイムゾーンがそのまま使われるため
  * JVM のタイムゾーンは明示的に指定したほうがよい気がする。
  *
  * @see https://github.com/scalikejdbc/scalikejdbc/blob/master/scalikejdbc-core/src/main/scala/scalikejdbc/UnixTimeInMillisConverter.scala#L26
  */
javaOptions += "-Duser.timezone=Asia/Tokyo"

/**
  * テスト時に読み込むlogbackの設定を切り替え
  */
javaOptions in Test += "-Dlogger.resource=logback-test.xml"

/**
  * テスト用のダミーの環境変数をセット
  */
javaOptions in Test += "-DTWITTER_CONSUMER_KEY=dummy"
javaOptions in Test += "-DTWITTER_CONSUMER_SECRET=dummy"
javaOptions in Test += "-DTWITTER_ACCESS_TOKEN=dummy"
javaOptions in Test += "-DTWITTER_ACCESS_TOKEN_SECRET=dummy"

/**
  * ScalaTest のオプション設定
  *
  * -oD : テストケースごとに実行時間の表示
  * -eI : 失敗したテストを最後にまとめて表示
  *
  * @see http://www.scalatest.org/user_guide/using_scalatest_with_sbt
  */
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD", "-eI")

/**
  * fork して実行するよう設定
  *
  * これを書かないと Task 実行時に正常終了してくれない。
  *
  * @see http://stackoverflow.com/questions/21464673/sbt-trapexitsecurityexception-thrown-at-sbt-run
  */
fork in (Compile, run) := true

// 依存ライブラリ
libraryDependencies ++= Dependencies.Application

// コンパイル時にドキュメントを含めない
// こうすると、コンパイルがちょっと高速になるかもしれない
// https://www.playframework.com/documentation/2.5.x/SBTCookbook#Disable-documentation
// sources in(Compile, doc) := Seq.empty

/**
  * ドキュメントにgraphvizの図を出力
  *
  * @see http://nantonaku-shiawase.hatenablog.com/entry/2013/12/04/010355
  */
scalacOptions in(Compile, doc) ++= Seq(
  "-groups",
  "-implicits",
  "-diagrams",
  "-skip-packages", Seq("router").mkString(":")
)

/**
  * API ドキュメントの自動リンク
  *
  * @see http://www.scala-sbt.org/0.13/docs/Howto-Scaladoc.html#Enable+automatic+linking+to+the+external+Scaladoc+of+managed+dependencies
  */
autoAPIMappings := true

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
  * Scalastyleでテストコード側もデフォルトでチェックするよう設定
  *
  * テストコードもプロダクトコード同様の品質にすべきである。
  *
  * 標準ではテストコードをチェックする場合 sbt test:scalastyle を叩くことになるが、
  * この設定を入れることで sbt scalastyle を叩いたときも、テストコードをチェックしてくれる。
  */
scalastyleSources in Compile <++= sourceDirectories in Test

/**
  * テスト時もついでに Scalastyle を実行するよう設定
  *
  * sbt scalastyle コマンドを実行した場合は compile 用の設定が読み込まれる。
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
wartremoverWarnings in(Compile, compile) ++= Warts.allBut(Wart.Overloading)
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
  * Scapegoat のバージョンを指定
  */
scapegoatVersion := "1.3.0"

/**
  * Scapegoat で除外する対象
  */
scapegoatIgnoredFiles := Seq(
  ".*/Routes.scala",
  ".*/JavaScriptReverseRoutes.scala",
  ".*/RoutesPrefix.scala",
  ".*/ReverseRoutes.scala"
)

/**
  * Scapegoat で除外する警告
  *
  * - 警告の除外設定
  *   - RedundantFinalModifierOnCaseClass : WartRemoverとぶつかるうえcase classの継承を許可するのが望ましいとは思えないので除外
  */
scapegoatDisabledInspections := Seq("RedundantFinalModifierOnCaseClass")

/**
  * CMD によるコピペチェックの設定
  *
  * これを入れないと、全部コピペチェックに引っかかる。
  * target ディレクトリも対象になっちゃってる？
  */
cpdSkipDuplicateFiles := true

/**
  * ここで設定した単語数以上が重複していたら、コピペチェックで引っかける
  *
  * デフォルトでは 100 と、やや大きめなので、少し小さめの値をセットする。
  *
  * @see https://github.com/sbt/cpd4sbt/blob/master/src/main/scala/de/johoop/cpd4sbt/Settings.scala#L33
  */
cpdMinimumTokens := 30


/**
  * sbt-updates の依存ライブラリアップデートチェックの対象外を設定
  *
  * dependencyUpdatesExclusions は非推奨になったから代わりに
  * dependencyUpdatesFilter を使えって書いてあるけど、なぜか使えない。。
  *
  * @see https://github.com/rtimush/sbt-updates#exclusions
  */
dependencyUpdatesExclusions := moduleFilter(organization = "org.scala-lang") |
  moduleFilter(name = "twirl-api") | // play のバージョンに依存しているので除外
  moduleFilter(name = "pmd-dist") // cpd はライブラリが更新されてないので除外

/**
  * sbt-updates の依存ライブラリアップデートチェック時に、アップデートすべきものがあればエラーにする
  */
dependencyUpdatesFailBuild := false

/**
  * sbt-dependency-check の出力先を変更
  */
dependencyCheckOutputDirectory := Some(file("target/analysis/"))

/**
  * sbt-dependency-graph の依存関係の表示から Scala 本体を除外
  */
filterScalaLibrary := false
