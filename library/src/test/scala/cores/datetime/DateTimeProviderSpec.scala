package cores.datetime

import java.time.LocalDateTime

import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.PlaySpec

import scala.util.control.Exception.ultimately

class DateTimeProviderSpec extends PlaySpec with BeforeAndAfterAll {

  // ScalaStyleでマジックナンバーの警告が出るが、ココについては、
  // マジックナンバーを再利用する予定はなく、直接書いたほうがリーダブルなので、警告を抑制している
  private val FixedDateTime = LocalDateTime.of(2016, 12, 31, 23, 59, 59) // scalastyle:ignore

  // テストが異常終了した場合も確実にClockを元に戻すため、念のためテストクラスの最後に実行
  override def afterAll(): Unit = {
    withDateTimeReset {
      super.afterAll()
    }
  }

  private def withDateTimeReset = {
    ultimately {
      FixedDateTimeProvider.reset()
    }
  }

  "DateTimeProvider#nowJST" should {
    "現在日時の固定ができること" in {
      // 現在日時を固定する前
      val actual1 = DateTimeProvider.nowJST()
      actual1.toString must not be "2016-12-31T23:59:59+09:00[Asia/Tokyo]"

      // 現在日時を固定
      FixedDateTimeProvider.fix(FixedDateTime, DateTimeProvider.JST)
      val actual2 = DateTimeProvider.nowJST()
      actual2.toString mustBe "2016-12-31T23:59:59+09:00[Asia/Tokyo]"

      // 現在日時の固定を解除
      FixedDateTimeProvider.reset()
      val actual3 = DateTimeProvider.nowJST()
      actual3.toString must not be "2016-12-31T23:59:59+09:00[Asia/Tokyo]"
    }
  }
}
