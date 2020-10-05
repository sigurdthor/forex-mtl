package forex.services.rates.interpreters

import cats.Applicative
import forex.Main.AppTask
import forex.domain.Rate
import forex.services.rates.Algebra
import zio.interop.catz._


class OneFrameDummy[F[_]] extends Algebra[F] {

  import forex.services.rates.clients.ForexClient.factory._

  override def get(pair: Rate.Pair): F[Rate] = retreiveRate(pair).orElseFail(new Exception())


}
