package base.scalatest

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}

trait ControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar {

}
