package forex.services.rates.interpreters

import forex.domain.Rate
import forex.services.rates.Algebra


class OneFrameDummy[F[_]] extends Algebra[F] {

  import forex.services.rates.clients.ForexClient.factory._

  override def get(pair: Rate.Pair): F[Rate] = retreiveRate(pair).mapError(_ => new Exception())


}
