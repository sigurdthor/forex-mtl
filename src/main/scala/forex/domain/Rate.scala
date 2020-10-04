package forex.domain

import cats.Show
import cats.implicits._

case class Rate(
    pair: Rate.Pair,
    price: Price,
    timestamp: Timestamp
)

object Rate {
  final case class Pair(
      from: Currency,
      to: Currency
  ) {
    implicit val show: Show[Pair] = Show.show { pair =>
      s"${pair.from.show}${pair.to.show}"
    }
  }
}
