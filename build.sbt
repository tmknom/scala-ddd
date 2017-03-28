name := """scala-ddd"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  // HTTP通信用ライブラリ
  // wsよりコッチを使うほうが推奨されているっぽい
  // http://qiita.com/bigwheel/items/44cb874ced4be204c09c
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

