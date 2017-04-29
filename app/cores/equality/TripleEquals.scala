package cores.equality

/**
  * 型チェックも含めてオブジェクトを比較
  *
  * Scala では通常 == でオブジェクトを比較するが、型チェックまではしない。
  * 例えば 5 == "5" のような記述もコンパイルを通ってしまうため Wartremover では == の使用を非推奨としている。
  *
  * 正直、ホントにコレでいくのか感があるが、一回従ってみる。
  * なお、同様の機構が ScalaUtils というライブラリで提供されており、クラス名はそこから拝借している。
  *
  * @see http://www.wartremover.org/doc/warts.html
  * @see http://www.scalautils.org/user_guide/CustomEquality
  */
object TripleEquals {
  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
    def ===(other: A): Boolean = self == other // scalastyle:ignore
  }
}
