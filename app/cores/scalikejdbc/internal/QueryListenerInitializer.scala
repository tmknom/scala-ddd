package cores.scalikejdbc.internal

import javax.inject.Singleton
import scalikejdbc.GlobalSettings

/**
  * [[QueryListener]] の初期化処理を担うクラス
  */
@Singleton
private[scalikejdbc] final class QueryListenerInitializer {
  /**
    * メソッド名や実装は [[scalikejdbc.PlayInitializer]] を参考にしている
    */
  private[this] def onStart(): Unit = {
    GlobalSettings.queryCompletionListener = QueryListener.queryCompletionListener
    GlobalSettings.queryFailureListener = QueryListener.queryFailureListener
  }

  // アプリケーション起動時に呼ばれる
  onStart()
}

