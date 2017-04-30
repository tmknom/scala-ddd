package cores.scalikejdbc

import javax.inject.Singleton

import scalikejdbc.GlobalSettings

/**
  * [[QueryListener]] の初期化処理を担うクラス
  */
@Singleton
final class QueryListenerInitializer {
  /**
    * メソッド名や実装は [[scalikejdbc.PlayInitializer]] を参考にしている
    */
  private[this] def onStart(): Unit = {
    GlobalSettings.queryCompletionListener = QueryListener.queryCompletionListener
  }

  // アプリケーション起動時に呼ばれる
  onStart()
}

