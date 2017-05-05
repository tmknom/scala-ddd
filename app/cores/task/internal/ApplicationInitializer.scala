package cores.task.internal

import play.api.{Application, ApplicationLoader, Environment, Mode}

private[task] object ApplicationInitializer {
  def initialize(): Application = {
    val environment = Environment(new java.io.File("."), this.getClass.getClassLoader, mode)
    val context = ApplicationLoader.createContext(environment)
    val loader = ApplicationLoader(context)
    loader.load(context)
  }

  private def mode: Mode.Mode = {
    System.getProperty("play.mode") match {
      case "Prod" => Mode.Prod
      case _ => Mode.Dev
    }
  }
}
