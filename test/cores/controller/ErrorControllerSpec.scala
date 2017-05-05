package cores.controller

import org.scalatestplus.play._

class ErrorControllerSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {
  "ErrorController#index" when {
    "success" in {
      go to ("http://localhost:" + port.toString + "/error")
      pageSource must include ("This is error!")
    }
  }
}
