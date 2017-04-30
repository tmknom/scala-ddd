package cores.scalikejdbc

import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}

final class QueryListenerModule extends Module {
  /**
    * [[QueryListener]] を有効化
    *
    * @param env    The environment
    * @param config The configuration
    * @return A sequence of bindings
    */
  override def bindings(env: Environment, config: Configuration): Seq[Binding[_]] = Seq(
    bind[QueryListenerInitializer].toSelf.eagerly
  )
}
