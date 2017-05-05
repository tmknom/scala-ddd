package cores.internal.constant

/**
  * リクエストヘッダーのタグ名を定数定義するクラス
  *
  * タグ名が大文字のスネークケースな理由は、同じくタグ名を定義している
  * [[play.api.routing.Router.Tags]] がそうなっているからである。
  */
private[cores] object RequestHeaderTagName {
  val RequestId: String = "REQUEST_ID"
}
