package cores.request

import cores.internal.request.RequestId
import org.scalatestplus.play.PlaySpec

class RequestIdSpec extends PlaySpec {
  "RequestId#initialize" should {
    "リクエストIDが生成できること" in {
      val actual = RequestId.initialize()
      actual.value.length mustBe 36
    }
  }
}
