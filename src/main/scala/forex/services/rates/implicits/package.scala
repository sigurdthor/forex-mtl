package forex.services.rates

import forex.Main.{ AppEnv, AppTask }

import scala.concurrent.Future

package object implicits {

  implicit class ZioToFuture[A](effect: AppTask[A]) {

    def toTaskFuture(implicit runtime: zio.Runtime[AppEnv]): Future[A] =
      runtime.unsafeRunToFuture(effect).future
  }
}
