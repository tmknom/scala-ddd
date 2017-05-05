package cores.task.internal

import cores.task.Task
import org.scalatestplus.play.PlaySpec
import play.api.{Application, Logger}

class ExecutionStartSpec extends PlaySpec {
  "ExecutionStart#start" should {
    "例外が起きないこと" in {
      noException mustBe thrownBy {
        ExecutionStart.start(TestTask)
      }
    }
  }

  private object TestTask extends Task {
    override def execute(app: Application): Unit = {
      Logger.trace("test")
    }
  }

}
