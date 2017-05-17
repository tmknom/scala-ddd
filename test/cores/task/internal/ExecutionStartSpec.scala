package cores.task.internal

import cores.task.Task
import org.scalatestplus.play.PlaySpec
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Logger}

import scala.util.control.Exception.{Catch, ultimately}

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
      val mockApp = MockBuilder.build()
      val injection = instanceOf[Injection](mockApp)
      Logger.trace(injection.toString)
    }

    override protected def withExit: Catch[Unit] = ultimately[Unit] {}
  }

  private object MockBuilder {
    def build(): Application = {
      new GuiceApplicationBuilder().
        overrides(bind[Injection].toSelf.eagerly()).
        build
    }
  }

  private class Injection {}

}
