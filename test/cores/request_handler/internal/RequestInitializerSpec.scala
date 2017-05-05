package cores.request_handler.internal

import cores.internal.constant.RequestHeaderTagName
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest

class RequestInitializerSpec extends PlaySpec {
  "RequestInitializer#initialize" should {
    "リクエストヘッダーにタグがセットされていること" in {
      val actual = RequestInitializer.initialize(FakeRequest())
      actual.tags(RequestHeaderTagName.RequestId).length mustBe 36
    }
  }
}
