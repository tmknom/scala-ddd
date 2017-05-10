package cores.spray

import cores.spray.TestUserJsonProtocol._
import org.scalatestplus.play.PlaySpec
import spray.json._

class UnderscoreJsonProtocolSpec extends PlaySpec {
  "UnderscoreJsonProtocol#extractFieldNames" should {
    "JSONのキー名がアンダースコア区切り（スネークケース）へ変換されること" in {

      val actual = TestUser("Jotaro Kujo", 30).toJson // scalastyle:ignore
      actual.compactPrint mustBe """{"user_name":"Jotaro Kujo","age":30}"""
    }
  }
}

private case class TestUser(userName: String, age: Int)

private object TestUserJsonProtocol extends UnderscoreJsonProtocol {
  implicit val format: RootJsonFormat[TestUser] = jsonFormat2(TestUser)
}
