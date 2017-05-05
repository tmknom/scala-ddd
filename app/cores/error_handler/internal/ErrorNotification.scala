package cores.error_handler.internal

import play.api.Logger
import play.api.mvc.RequestHeader

/**
  * エラー用の通知
  */
private[error_handler] object ErrorNotification {

  /**
    * エラーを通知する
    *
    * @param requestHeader リクエストヘッダー
    * @param throwable     スローされた例外
    */
  def notify(requestHeader: RequestHeader, throwable: Throwable): Unit = {
    Logger.trace(s"please implement me! - ${this.getClass.getSimpleName}#notify")
  }
}
