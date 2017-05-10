package cores.spray

import cores.spray.internal.StringConversion
import spray.json._

import scala.reflect.ClassTag

/**
  * JSON のキー名をアンダースコア区切り（スネークケース）へ変換
  *
  * JsonProtocol のクラス作成時に、本トレイトをミックスインして使う。
  * 自前の JsonProtocol を定義していない場合 import cores.spray.UnderscoreJsonProtocol._ する。
  *
  * @see http://qiita.com/suin/items/70b1d1ee99a595cc07fe
  */
trait UnderscoreJsonProtocol extends DefaultJsonProtocol {

  /**
    * This is the most important piece of code in this object!
    * It overrides the default naming scheme used by spray-json and replaces it with a scheme that turns camelcased
    * names into snakified names (i.e. using underscores as word separators).
    */
  override protected def extractFieldNames(classTag: ClassTag[_]): Array[String] = {
    super.extractFieldNames(classTag).map(StringConversion.underscore)
  }
}

object UnderscoreJsonProtocol extends UnderscoreJsonProtocol
