package cores.task

import cores.task.internal.{ApplicationInjector, Execution, ExecutionStart}

/**
  * コマンドラインから実行可能なアプリケーション
  *
  * バッチ処理などを実装したい場合は、
  * 本クラスを extends したクラスを実装する。
  */
trait Task extends App with Execution with ApplicationInjector {
  ExecutionStart.start(this)
}
