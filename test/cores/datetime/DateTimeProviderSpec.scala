package cores.datetime

import java.time.{LocalDateTime, ZoneId}

import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.PlaySpec

import scala.util.control.Exception.ultimately

class DateTimeProviderSpec extends PlaySpec with BeforeAndAfterEach {
  private val FixedDateTime = LocalDateTime.of(2016, 12, 31, 23, 59, 59) // scalastyle:ignore

  /**
    * テストケースごとに現在日時を固定する
    */
  override def beforeEach(): Unit = {
    DateTimeProvider.useFixedClockForTest(FixedDateTime)

    super.beforeEach() // To be stackable, must call super.beforeEach
  }

  /**
    * テストケースごとに現在日時をリセットする
    */
  override def afterEach(): Unit = {
    withFinally {
      super.afterEach() // To be stackable, must call super.afterEach
    }
  }

  private def withFinally = {
    ultimately {
      DateTimeProvider.useSystemClockForTest()
    }
  }

  "now" should {
    "LocalDateTime" in {
      val actual = DateTimeProvider.now()

      actual.toString mustBe "2016-12-31T23:59:59"
    }
  }

  "now" should {
    "ZonedDateTime" in {
      val actual = DateTimeProvider.now(ZoneId.of("Australia/Darwin"))

      actual.toString mustBe "2017-01-01T00:29:59+09:30[Australia/Darwin]"
    }
  }
}
