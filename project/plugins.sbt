// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.14")

// JS/CSS絡みは無効にしておく
// どうせREST APIしか実装しないのでこの記述は不要
//
// web plugins
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.4")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.8")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")
//
//addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")

/**
  * カバレッジ
  *
  * @see https://github.com/scoverage/sbt-scoverage
  */
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

/**
  * カバレッジ表示：Coveralls
  *
  * 事前準備として coveralls 上で発行したトークンを CircleCI の Environment Variables に
  * COVERALLS_REPO_TOKEN という名前で保存しておく必要がある。
  *
  * @see https://github.com/scoverage/sbt-coveralls
  */
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.1.0")

/**
  * がバレッジ＆静的解析：Codacy
  *
  * 事前準備として Codacy 上で発行したトークンを CirclecCI の Environment Variables に
  * CODACY_PROJECT_TOKEN という名前で保存しておく必要がある。
  *
  * @see https://github.com/codacy/sbt-codacy-coverage
  */
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.8")

/**
  * 静的解析ツール：Scalastyle
  *
  * @see http://www.scalastyle.org/sbt.html
  */
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

/**
  * 静的解析ツール：WartRemover
  *
  * @see http://www.wartremover.org/
  */
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.0.3")

/**
  * 静的解析ツール：Scapegoat
  *
  * @see https://github.com/sksamuel/sbt-scapegoat
  */
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")

/**
  * コピペチェック
  *
  * @see https://github.com/sbt/cpd4sbt
  */
addSbtPlugin("de.johoop" % "cpd4sbt" % "1.2.0")

/**
  * 依存ライブラリのアップデート確認
  *
  * @see https://github.com/rtimush/sbt-updates
  */
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")

/**
  * 依存ライブラリの脆弱性チェック
  *
  * @see https://github.com/albuch/sbt-dependency-check
  */
addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "0.1.7")

/**
  * プロジェクトの統計情報取得
  *
  * @see https://github.com/orrsella/sbt-stats
  */
addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5")

/**
  * 依存関係の描画
  *
  * @see https://github.com/jrudolph/sbt-dependency-graph
  */
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
