name := """scala-ddd"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  // DB接続用でおなじみ
  jdbc,
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
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

