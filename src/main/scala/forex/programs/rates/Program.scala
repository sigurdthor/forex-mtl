package forex.programs.rates

import forex.domain._
import forex.services.RatesService

class Program[F[_]](
    ratesService: RatesService[F]
) extends Algebra[F] {

  override def get(request: Protocol.GetRatesRequest): F[Rate] =
    ratesService.get(Rate.Pair(request.from, request.to))

}

object Program {

  def apply[F[_]](
      ratesService: RatesService[F]
  ): Algebra[F] = new Program[F](ratesService)

}
