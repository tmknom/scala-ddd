package cores.test.scalatest

import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.db.DBApi
import play.api.db.evolutions.Evolutions

import scala.util.control.Exception._

/**
  * データベースとの結合テスト用trait
  *
  * 現状
  *  ・テストケースごとにマイグレーションを実行し、create tableが行われる
  *  ・毎回、テーブル自体がDropされるので、クリーンな状態で毎回テストできる
  *  ・スローテスト問題に結構早くぶち当たる
  *
  * こうしたい
  *  ・sbt test コマンド実行時
  *  　・create table：テスト起動前に、一回だけ実行する
  *  　・drop table：すべてのテスト実行終了後、一回だけ実行する
  *  ・参照系のみの場合
  *  　・テストクラス単位でデータベースフィクスチャのセットアップ／クリーンアップを実行する
  *    ・つまり、beforeAllメソッドとafterAllメソッドを使用する
  *  ・更新系を含む場合
  *  　・Test Case 単位でデータベースフィクスチャのセットアップ／クリーンアップを実行する
  *    ・つまり、beforeEachメソッドとafterEachメソッドを使用する
  */
trait DatabaseSpec extends PlaySpec with OneAppPerSuite with BeforeAndAfterEach {
  /**
    * テスト実行前に、マイグレーションを実行し、DBをセットアップ
    *
    * テストケースごとに実行するので、DBのテストが多くなると重くなる。
    * 更新系のテストをするときは、テストケースごとに毎回DBをクリーンにしたほうが良いが、
    * 参照系のテストの場合は、Specクラス単位でセットアップしたほうが良いかもしれない。
    */
  override def beforeEach(): Unit = {
    Evolutions.applyEvolutions(databaseApi.database("default"))

    // BeforeAndAfterEachの定義を読むとこのコードは必須な模様。
    // おそらく、複数のtraitを積み重ねた時に、正しく動作するようにするためだと思うが
    // traitについての正しい知見がないため、そんなに自信はない。
    super.beforeEach() // To be stackable, must call super.beforeEach
  }

  /**
    * テスト実行後に、cleanupマイグレーションを実行し、DBをクリア
    */

  override def afterEach(): Unit = {
    withFinally {
      super.afterEach()
    }
  }

  // Javaで言うfinallyだぞ
  // http://seratch.hatenablog.jp/entry/20111126/1322309305
  private def withFinally = {
    ultimately {
      Evolutions.cleanupEvolutions(databaseApi.database("default"))
    }
  }

  // Google Guiceのinjectionをテスト内で実行するためのおまじない
  private lazy val databaseApi: DBApi = app.injector.instanceOf[DBApi] //here is the important line
}
