package cores.internal.request

import cores.internal.constant.RequestHeaderTagName
import play.api.mvc.RequestHeader

/**
  * リクエストIDの格納／取り出し
  */
private[cores] object RequestIdStore {
  /**
    * リクエストヘッダーからリクエストIDを取り出す
    *
    * @param requestHeader リクエストヘッダー
    * @return リクエストID
    */
  def extract(requestHeader: RequestHeader): RequestId = {
    val value = requestHeader.tags(RequestHeaderTagName.RequestId)
    RequestId(value)
  }
}
