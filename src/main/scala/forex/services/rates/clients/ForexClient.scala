package forex.services.rates.clients

import cats.implicits._
import forex.config.ApplicationConfig
import forex.domain.Rate
import forex.services.rates.errors.ForexError
import forex.services.rates.errors.ForexError.{MalformedUrl, OneFrameLookupFailed}
import forex.services.rates.implicits.JsonOps._
import izumi.logstage.api.IzLogger
import izumi.logstage.api.Log.Level.Trace
import izumi.logstage.sink.ConsoleSink
import logstage.{LogBIO, LogstageZIO}
import org.http4s._
import org.http4s.client.Client
import zio._
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._

object ForexClient {

  type ForexClient = Has[ForexClient.Service]

  trait Service {
    def retreiveRate(pair: Rate.Pair): IO[ForexError, Rate]
  }

  def live(client: Client[Task], config: ApplicationConfig, maybeToken: Option[String]): Layer[Nothing, ForexClient] =
    ZLayer.succeed(new Service {

      lazy val textSink           = ConsoleSink.text(colored = true)
      lazy val izLogger: IzLogger = IzLogger(Trace, List(textSink))
      lazy val log: LogBIO[IO]    = LogstageZIO.withFiberId(izLogger)

      override def retreiveRate(pair: Rate.Pair): IO[ForexError, Rate] =
        performRequest(show"${config.client.url}/rates?pair=$pair")

      private def performRequest(uri: String): IO[ForexError, Rate] = {
        def call(uri: Uri): IO[ForexError, Rate] = {

          val headers = maybeToken match {
            case Some(token) => Headers.of(Header("token", token))
            case None        => Headers.empty
          }

          log.debug(s"Performing request with uri: $uri") *>
            client
              .expect[String](Request[Task](Method.GET, uri, headers = headers))
              .foldM(
                ex => log.error(s"Request error: ${ex.getMessage}") *> IO.fail(OneFrameLookupFailed(ex.getMessage)),
                response => log.debug(s"Response data: $response") *> response.toRate
              )
        }

        Uri
          .fromString(uri)
          .fold(_ => IO.fail(MalformedUrl(uri)), call)
      }
    })

  object factory {

    def retreiveRate(pair: Rate.Pair): ZIO[ForexClient with Clock with Console, ForexError, Rate] =
      ZIO.accessM(_.get.retreiveRate(pair))
  }

}
