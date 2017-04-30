// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.10")

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
  * 依存ライブラリのアップデート確認
  *
  * @see https://github.com/rtimush/sbt-updates
  */
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")
