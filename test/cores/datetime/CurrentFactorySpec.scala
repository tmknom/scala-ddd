package cores.datetime

import java.time._

import org.scalatest.{BeforeAndAfterEach, Suite}
import org.scalatestplus.play.PlaySpec

class CurrentFactorySpec extends PlaySpec with FixedDateTime {

  override def createZonedDateTime(): ZonedDateTime = { defaultZonedDateTime() }

  "factoryLocalDateTime" should {
    "success" in {
      val actual = CurrentFactory.factoryLocalDateTime()

      actual.toString mustBe "2016-12-31T23:59:59"
    }
  }

  "factoryZonedDateTime" should {
    "success" in {
      val actual = CurrentFactory.factoryZonedDateTime(ZoneId.of("Australia/Darwin"))

      actual.toString mustBe "2017-01-01T00:29:59+09:30[Australia/Darwin]"
    }
  }

  "factoryLocalTime" should {
    "success" in {
      val actual = CurrentFactory.factoryLocalTime()

      actual.toString mustBe "23:59:59"
    }
  }

  "factoryLocalDate" should {
    "success" in {
      val actual = CurrentFactory.factoryLocalDate()

      actual.toString mustBe "2016-12-31"
    }
  }
}

trait FixedDateTime extends BeforeAndAfterEach {
  this: Suite =>

  /**
    * このメソッドをオーバーライドすると、任意の時間に固定できる
    *
    * @return 固定した現在日時
    */
  def createZonedDateTime(): ZonedDateTime

  /**
    * デフォルトの固定日時
    */
  def defaultZonedDateTime(): ZonedDateTime = {
    ZonedDateTime.of(DEFAULT_LOCAL_DATE_TIME, ZoneId.systemDefault)
  }
  private val DEFAULT_LOCAL_DATE_TIME = LocalDateTime.of(2016, 12, 31, 23, 59, 59) // scalastyle:ignore

  /**
    * テストケースごとに現在日時を固定する
    */
  override def beforeEach(): Unit = {
    val instant = createZonedDateTime().toInstant
    val fixedClock = Clock.fixed(instant, ZoneId.systemDefault)
    CurrentFactory.setForTest(fixedClock)

    super.beforeEach() // To be stackable, must call super.beforeEach
  }

  /**
    * テストケースごとに現在日時をリセットする
    */
  override def afterEach(): Unit = {
    try {
      super.afterEach() // To be stackable, must call super.afterEach
    } finally {
      CurrentFactory.resetForTest()
    }
  }
}
