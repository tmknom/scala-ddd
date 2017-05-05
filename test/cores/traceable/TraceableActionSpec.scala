package cores.traceable

import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.PlaySpec
import org.slf4j.MDC
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class TraceableActionSpec extends PlaySpec with BeforeAndAfterEach {
  private val MdcKey = "sampleKey"

  override def afterEach(): Unit = {
    MDC.remove(MdcKey)
  }

  "TraceableAction" should {
    "MDCから値が取得できること" in {
      MDC.put(MdcKey, "value")
      val actual = contentAsString((new TraceableController).index().apply(FakeRequest()))
      actual mustBe "value"
    }
  }

  private class TraceableController extends Controller {
    def index: Action[AnyContent] = TraceableAction {
      Ok(MDC.get(MdcKey))
    }
  }
}
