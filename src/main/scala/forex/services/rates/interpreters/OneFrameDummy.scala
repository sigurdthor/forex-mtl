package forex.services.rates.interpreters

import cats.implicits._
import forex.Main.{AppEnv, AppTask}
import forex.domain._
import forex.services.rates.Algebra
import forex.services.rates.implicits._
import scalacache.Cache
import scalacache.caffeine.CaffeineCache
import scalacache.memoization._
import scalacache.modes.scalaFuture._
import zio.IO

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class OneFrameDummy(implicit runtime: zio.Runtime[AppEnv]) extends Algebra[AppTask] {

  import forex.services.rates.clients.ForexClient.factory._

  type Key = String

  implicit val cache: Cache[Rate] = CaffeineCache[Rate]
  val ttl                         = 5.minute

  override def get(pair: Rate.Pair): AppTask[Rate] =
    IO.fromFuture { implicit ec =>
      getRate(pair)
    }

  private def getRate(pair: Rate.Pair)(implicit ec: ExecutionContext): Future[Rate] =
    memoizeF(ttl.some)(retreiveRate(pair).toTaskFuture)
}
