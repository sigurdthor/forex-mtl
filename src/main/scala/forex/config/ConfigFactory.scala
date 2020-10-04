package forex.config

import pureconfig.ConfigSource
import zio.{UIO, ZIO}
import pureconfig.generic.auto._

object ConfigFactory {

  /**
   * @param path the property path inside the default configuration
   */
  def load(path: String): UIO[ApplicationConfig] = {
    ZIO.succeed(
      ConfigSource.default.at(path).loadOrThrow[ApplicationConfig])
  }

}
