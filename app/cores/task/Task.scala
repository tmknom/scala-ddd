package cores.task

import cores.task.internal.{ApplicationInjector, Execution, ExecutionStart}

import scala.util.control.Exception.{Catch, ultimately}

/**
  * コマンドラインから実行可能なアプリケーション
  *
  * バッチ処理などを実装したい場合は、
  * 本クラスを extends したクラスを実装する。
  */
trait Task extends App with Execution with ApplicationInjector {
  withExit {
    ExecutionStart.start(this)
  }

  /**
    * JVM の終了
    *
    * 終了処理を書かないとタスクが正常終了してくれない。（プロセスが刺さった状態になる）
    * 毎回 Ctrl + C を押すのは不便すぎるので、明示的に JVM を終了させることにした。
    * ただし、この実装だと、例外発生時も exit code が 0 になってしまうことに注意。
    *
    * @see http://stackoverflow.com/questions/21464673/sbt-trapexitsecurityexception-thrown-at-sbt-run
    */
  protected def withExit: Catch[Unit] = {
    ultimately[Unit] {
      sys.exit()
    }
  }
}
