package cores.error_handler.internal

import cores.internal.request.RequestId
import play.api.libs.json.{JsObject, Json}

/**
  * エラーレスポンス用のJSONを生成するクラス
  */
private[error_handler] object ErrorRenderer {
  /**
    * エラーレスポンス用のJSONを生成
    *
    * @param throwable  スローされた例外
    * @param statusCode HTTPステータスコード
    * @param requestId  リクエストID
    * @return エラーJSON
    */
  def render(throwable: Throwable, statusCode: Int, requestId: RequestId): JsObject = {
    Json.obj(
      "errors" -> Json.arr(
        Json.obj(
          "code" -> throwable.getClass.getSimpleName,
          "message" -> throwable.getMessage
        )
      ),
      "status_code" -> statusCode,
      "request_id" -> requestId.value
    )
  }
}
