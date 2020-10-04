package forex.services.rates

object errors {

  sealed trait ForexError {
    def msg: String
  }

  object ForexError {
    final case class OneFrameLookupFailed(msg: String) extends ForexError

    final case class MalformedUrl(url: String) extends ForexError {
      override def msg: String = s"Couldn't build url for repository: $url"
    }
  }

}
