/**
  * 静的解析
  */

import sbt.Keys.{compile, test}
import sbt.{Compile, Test}

object StaticAnalysis {
  val Settings = WartRemover.Settings

  object WartRemover {

    import play.sbt.routes.RoutesKeys.routes
    import wartremover.WartRemover.autoImport._

    val Settings = Seq(
      /**
        * WartRemover のプロダクトコード側の設定
        *
        * - 警告の除外設定
        *   - Overloading : オーバーロードは普通に使われるものであるため除外
        * - 警告の除外ファイル
        *   - routes : ルーティング設定は事実上のDSLなのでチェックしない
        *
        * @see http://www.wartremover.org/doc/install-setup.html
        */
      wartremoverWarnings in(Compile, compile) ++= Warts.allBut(Wart.Overloading),

      // 本当は右記のように書こうとした => wartremoverExcluded += baseDirectory.value / "conf" / "routes"
      // が、除外してくれなかったので、このような書き方に落ち着いた。たぶん、conf配下のファイルは扱いが特殊なんだろう。
      // http://stackoverflow.com/questions/34788530/wartremover-still-reports-warts-in-excluded-play-routes-file
      wartremoverExcluded ++= routes.in(Compile).value,

      /**
        * WartRemover のテストコード側の設定
        *
        * - 警告の除外設定
        *   - Overloading : オーバーロードは許可しておかないと、テストのbeforeEachとか怒られるので除外
        *   - OptionPartial : コントローラでよく出てくるイディオム route(app, FakeRequest(GET, "/any/url")).get が引っかかるため除外
        *
        * - OptionPartial を抑制する詳細な理由
        *   - 抑制しない場合、コントローラのテストなどで「Option#get is disabled - use Option#fold instead」という警告が出る。
        *   - プロダクトコードでは確かに、Option 型で get メソッドを使わず、fold メソッドを使うというのは良い習慣に思える。
        *   - 一方で、コントローラのテストでは route(app, FakeRequest(GET, "/any/url")).get というイディオムがよく出てくる。
        *   - コントローラのテストでは、fold メソッドなどを使うと逆にテストの見通しが悪くなるように見える。
        *   - よって、テストコードでは OptionPartial を抑制することにした。
        *   - 本当はコントローラのテストコードだけ指定とかできればよいが、そこは妥協することとした。
        */
      wartremoverErrors in(Test, test) ++= Warts.allBut(Wart.Overloading, Wart.OptionPartial)
    )
  }

}
