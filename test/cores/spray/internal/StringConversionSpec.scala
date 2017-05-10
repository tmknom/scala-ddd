package cores.spray.internal

import org.scalatestplus.play.PlaySpec

class StringConversionSpec extends PlaySpec {
  "StringConversion#underscore" should {
    "空文字の場合" in {
      val actual = StringConversion.underscore("")
      actual mustBe ""
    }

    "小文字のみの場合" in {
      val actual = StringConversion.underscore("user")
      actual mustBe "user"
    }

    "最初小文字のキャメルケースの場合" in {
      val actual = StringConversion.underscore("userMailAddress")
      actual mustBe "user_mail_address"
    }

    "最初大文字のキャメルケースの場合" in {
      val actual = StringConversion.underscore("UserMailAddress")
      actual mustBe "user_mail_address"
    }

    "大文字のみのスネークケースの場合" in {
      val actual = StringConversion.underscore("USER_MAIL_ADDRESS")
      actual mustBe "user_mail_address"
    }

    "小文字のみのスネークケースの場合" in {
      val actual = StringConversion.underscore("user_mail_address")
      actual mustBe "user_mail_address"
    }
  }
}
