package forex.services.rates

import forex.Main.{AppEnv, AppTask}
import forex.services.rates.interpreters._

object Interpreters {
  def dummy(implicit runtime: zio.Runtime[AppEnv]): Algebra[AppTask] = new OneFrameDummy()
}
