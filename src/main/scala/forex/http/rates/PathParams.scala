package forex.http.rates

import forex.domain.Currency
import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.QueryParamDecoderMatcher

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
