package cores.request_handler.internal

import cores.internal.constant.RequestHeaderTagName
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
