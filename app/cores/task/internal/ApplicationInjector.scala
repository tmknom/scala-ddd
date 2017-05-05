package cores.task.internal

import play.api.Application

import scala.reflect.ClassTag

private[task] trait ApplicationInjector {
  protected[this] def instanceOf[T: ClassTag](app: Application): T = {
    app.injector.instanceOf[T]
  }
}
