package forex.services.rates.implicits

import forex.domain.Rate.Pair
import forex.domain.{Currency, Price, Rate, Timestamp}
import forex.http.rates.Protocol._
import forex.services.rates.errors.ForexError
import forex.services.rates.errors.ForexError.{JsonParsingError, OneFrameLookupFailed}
import io.circe.Json
import io.circe.optics.JsonPath.root
import io.circe.parser.parse
import zio.IO

object Json {

  case class RateJson(from: Currency, to: Currency, bid: Price, ask: Price, price: Price, timestamp: Timestamp)

  implicit class RateDecoder(jsonString: String) {

    def toRate: IO[ForexError, Rate] =
      parse(jsonString) match {
        case Left(failure) => IO.fail(OneFrameLookupFailed(failure.message))
        case Right(json) =>
          root.error.string.getOption(json) match {
            case Some(error) => IO.fail(OneFrameLookupFailed(error))
            case None        => decodeRate(json)
          }

      }

    private def decodeRate(json: Json): IO[ForexError, Rate] =
      json.as[Seq[RateJson]] match {
        case Left(error) => IO.fail(JsonParsingError(error.message))
        case Right(items) =>
          IO.fromOption(items.headOption.map(r => Rate(Pair(r.from, r.to), r.price, r.timestamp)))
            .orElseFail(OneFrameLookupFailed("Empty response"))
      }
  }

}
