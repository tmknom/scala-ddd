package cores.controller

import javax.inject._

import play.api.mvc._

@Singleton
@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements", "org.wartremover.warts.Throw"))
final class ErrorController extends Controller {
  def index: Action[AnyContent] = Action {
    throw new RuntimeException("This is error!")
    Ok("ここには絶対に到達しないが、この記述がないとコンパイルエラーになるので書いておく")
  }
}
