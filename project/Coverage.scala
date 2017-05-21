/**
  * カバレッジ
  */

import scoverage.ScoverageKeys.{coverageExcludedPackages, coverageFailOnMinimum, coverageMinimum}

object Coverage {
  val Settings = Seq(
    /**
      * カバレッジの除外対象
      *
      * 自動生成されるroutes系のパッケージを除外する。
      * 除外対象を追加したい場合、正規表現で対象パッケージを指定できる。
      * ファイル単位で除外対象を指定したい場合は coverageExcludedFiles を使う。
      *
      * @see https://github.com/scoverage/sbt-scoverage#exclude-classes-and-packages
      */
    coverageExcludedPackages := Seq(
      "router\\.*",
      ".*Reverse.*"
    ).mkString(";"),

    /**
      * カバレッジが coverageMinimum で指定した値より下回ったらビルドを失敗させる
      *
      * デフォルト値は false なので明示的に指定する。
      *
      * @see https://github.com/scoverage/sbt-scoverage#minimum-coverage
      */
    coverageFailOnMinimum := true,

    /**
      * カバレッジで維持したい最小値を設定
      *
      * デフォルト値は 0 なので明示的に指定する。
      *
      * @see https://github.com/scoverage/sbt-scoverage/blob/master/src/main/scala/scoverage/ScoverageSbtPlugin.scala#L30
      */
    coverageMinimum := 75
  )
}



