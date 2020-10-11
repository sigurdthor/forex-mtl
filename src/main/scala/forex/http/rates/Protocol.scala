package forex.http
package rates

import forex.domain.Currency.show
import forex.domain.Rate.Pair
import forex.domain._
import forex.services.rates.implicits.JsonOps.RateJson
import io.circe._
import io.circe.generic.semiauto._

object Protocol {

  final case class GetApiRequest(
      from: Currency,
      to: Currency
  )

  final case class GetApiResponse(
      from: Currency,
      to: Currency,
      price: Price,
      timestamp: Timestamp
  )

  final case class ErrorResponse(error: String)

  implicit val currencyEncoder: Encoder[Currency] =
    Encoder.instance[Currency] { show.show _ andThen Json.fromString }

  implicit val pairEncoder: Encoder[Pair] =
    deriveEncoder[Pair]

  implicit val pairDecoder: Decoder[Pair] =
    deriveDecoder[Pair]

  implicit val rateJsonDecoder: Decoder[RateJson] = deriveDecoder[RateJson]

  implicit val rateEncoder: Encoder[Rate] = deriveEncoder[Rate]

  implicit val rateDecoder: Decoder[Rate] = deriveDecoder[Rate]

  implicit val responseEncoder: Encoder[GetApiResponse] =
    deriveEncoder[GetApiResponse]

  implicit val errorEncoder: Encoder[ErrorResponse] =
    deriveEncoder[ErrorResponse]
}
