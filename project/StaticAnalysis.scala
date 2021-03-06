/**
  * 静的解析
  */

import sbt.Keys._
import sbt._

// noinspection TypeAnnotation
object StaticAnalysis {
  val Settings = WartRemover.Settings ++ Scapegoat.Settings ++ CPD.Settings

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

  object Scapegoat {

    import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._

    val Settings = Seq(
      /**
        * Scapegoat のバージョンを指定
        */
      scapegoatVersion := "1.3.0",

      /**
        * Scapegoat で除外する対象
        */
      scapegoatIgnoredFiles := Seq(
        ".*/Routes.scala",
        ".*/JavaScriptReverseRoutes.scala",
        ".*/RoutesPrefix.scala",
        ".*/ReverseRoutes.scala"
      ),

      /**
        * Scapegoat で除外する警告
        *
        * - 警告の除外設定
        *   - RedundantFinalModifierOnCaseClass : WartRemoverとぶつかるうえcase classの継承を許可するのが望ましいとは思えないので除外
        */
      scapegoatDisabledInspections := Seq("RedundantFinalModifierOnCaseClass")
    )
  }

  object CPD {

    import de.johoop.cpd4sbt.CopyPasteDetector.autoImport._

    val Settings = Seq(
      /**
        * CPD によるコピペチェックの設定
        *
        * これを入れないと、全部コピペチェックに引っかかる。
        * target ディレクトリも対象になっちゃってる？
        */
      cpdSkipDuplicateFiles := true,

      /**
        * ここで設定した単語数以上が重複していたら、コピペチェックで引っかける
        *
        * デフォルトでは 100 と、やや大きめなので、少し小さめの値をセットする。
        *
        * @see https://github.com/sbt/cpd4sbt/blob/master/src/main/scala/de/johoop/cpd4sbt/Settings.scala#L33
        */
      cpdMinimumTokens := 30
    )
  }

}
