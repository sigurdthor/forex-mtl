package forex.http.rates

import forex.domain._
import forex.services.rates.errors.ForexError

object Converters {
  import Protocol._

  private[rates] implicit class GetApiResponseOps(val rate: Rate) extends AnyVal {
    def asGetApiResponse: GetApiResponse =
      GetApiResponse(
        from = rate.pair.from,
        to = rate.pair.to,
        price = rate.price,
        timestamp = rate.timestamp
      )
  }

  private[rates] implicit class GetErrorResponseOps(val error: ForexError) extends AnyVal {
    def asErrorResponse: ErrorResponse = ErrorResponse(error.msg)
  }

}
