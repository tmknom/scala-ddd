package cores.test.fixture

import scala.io.Source
import scala.util.control.Exception.ultimately

/**
  * Fixtureをロードするユーティリティクラス
  *
  * test/fixtures ディレクトリ配下のファイルをロード
  */
object FixtureLoader {
  /**
    * Fixture をロードする
    *
    * 例えば test/fixtures/hoge/fuga.txt をロードする場合
    * val fixture = FixtureLoader.load("/hoge/fuga")
    *
    * @param path フィクスチャのパス
    * @return フィクスチャ
    */
  def load(path: String): String = {
    val source = Source.fromInputStream(getClass.getResourceAsStream(path))
    withFinally(source: Source) {
      source.mkString
    }
  }

  private def withFinally(source: Source) = {
    ultimately {
      source.close()
    }
  }
}
