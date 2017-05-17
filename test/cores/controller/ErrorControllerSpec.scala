package cores.controller

import org.scalatestplus.play._
import play.api.libs.json.Json

class ErrorControllerSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {
  "ErrorController#index" when {
    "success" in {
      go to ("http://localhost:" + port.toString + "/error")

      val actual = Json.parse(pageSource)
      val requestId = (actual \ "request_id").as[String]

      // リクエストIDは毎回変わるので長さだけ確認
      requestId.length mustBe 36

      // リクエストIDは動的に埋め込まないといけない
      actual mustBe Json.parse(
        s"""{
           |  "errors": [{
           |    "code": "anon$$1",
           |    "message": "Execution exception[[RuntimeException: This is error!]]"
           |  }],
           |  "status_code": 500,
           |  "request_id": "$requestId"
           |}""".stripMargin)
    }
  }
}
