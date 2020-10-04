package forex.programs.rates

import forex.services.rates.errors.{ ForexError => RatesServiceError }

object errors {

  sealed trait Error extends Exception
  object Error {
    final case class RateLookupFailed(msg: String) extends Error
  }

  def toProgramError(error: RatesServiceError): Error = Error.RateLookupFailed(error.msg)
}
