package forex.services.rates.interpreters

import cats.implicits._
import forex.Main.{ AppEnv, AppTask }
import forex.domain._
import forex.services.rates.Algebra
import scalacache.Cache
import scalacache.caffeine.CaffeineCache
import scalacache.memoization._
import scalacache.serialization.circe._
import forex.http.rates.Protocol._
import scalacache.modes.scalaFuture._
import zio.IO
import forex.services.rates.implicits._

import scala.concurrent.duration._

class OneFrameDummy(implicit runtime: zio.Runtime[AppEnv]) extends Algebra[AppTask] {

  import forex.services.rates.clients.ForexClient.factory._

  type Key = String

  implicit val cache: Cache[Rate] = CaffeineCache[Rate]
  val ttl                         = 5.minute

  override def get(pair: Rate.Pair): AppTask[Rate] =
    IO.fromFuture { implicit ec =>
      memoizeF(ttl.some)(retreiveRate(pair).toTaskFuture)
    }
}
