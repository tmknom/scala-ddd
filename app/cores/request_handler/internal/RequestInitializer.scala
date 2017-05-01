package cores.request_handler.internal

import cores.request.RequestId
import play.api.mvc.RequestHeader

/**
  * RequestHeader オブジェクトにタグ情報を追加して初期化するクラス
  */
private[request_handler] object RequestInitializer {
  /**
    * リクエストヘッダーにタグを追加して初期化
    *
    * @param requestHeader リクエストヘッダー
    * @return タグ追加済みのリクエストヘッダー
    */
  def initialize(requestHeader: RequestHeader): RequestHeader = {
    val requestId = RequestId.initialize()
    requestHeader.withTag(RequestHeaderTagName.RequestId, requestId.value)
  }
}

/**
  * リクエストヘッダーのタグ名を定数定義するクラス
  *
  * タグ名が大文字のスネークケースな理由は、同じくタグ名を定義している
  * [[play.api.routing.Router.Tags]] がそうなっているからである。
  */
private[internal] object RequestHeaderTagName {
  val RequestId: String = "REQUEST_ID"
}
