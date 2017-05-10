package cores.spray.internal

import java.util.Locale

/**
  * 文字列変換クラス
  *
  * spray 以外でも使うかもしれないので、最初からクラスだけ分けた。
  * 今は spray からしか使われないので、アクセス修飾子もそうしているが、
  * ほかで使うのであれば、アクセス修飾子を緩めれば良いだろう。
  */
private[spray] object StringConversion {
  /**
    * 入力文字列をアンダースコア区切り（スネークケース）に変換
    *
    * @param word 変換対象文字列
    * @return アンダースコア区切りの文字列
    */
  def underscore(word: String): String = {
    SecondPattern.replaceAllIn(
      FirstPattern.replaceAllIn(
        word, ReplacementPattern), ReplacementPattern).toLowerCase(Locale.US)
  }

  private val FirstPattern = "([A-Z]+)([A-Z][a-z])".r
  private val SecondPattern = "([a-z\\d])([A-Z])".r
  private val ReplacementPattern = "$1_$2"
}
