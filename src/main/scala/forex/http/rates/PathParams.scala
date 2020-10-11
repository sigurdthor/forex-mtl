package forex.http.rates

import forex.domain.Currency

import scala.util.Try

object PathParams {

  object CurrencyVar {
    def unapply(str: String): Option[Currency] =
      if (!str.isEmpty)
        Try(Currency.fromString(str)).toOption
      else
        None
  }
}
