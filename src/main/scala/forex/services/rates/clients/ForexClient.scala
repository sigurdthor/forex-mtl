package forex.services.rates.clients

import forex.Main.AppEnv
import forex.domain.Rate
import forex.services.rates.errors.ForexError
import forex.services.rates.errors.ForexError.{MalformedUrl, OneFrameLookupFailed}
import io.circe.Decoder
import io.circe.generic.auto._
import izumi.logstage.api.IzLogger
import izumi.logstage.api.Log.Level.Trace
import izumi.logstage.sink.ConsoleSink
import logstage.{LogBIO, LogstageZIO}
import org.http4s._
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import zio.{Has, IO, Layer, Task, ZEnv, ZIO, ZLayer}
import zio.interop.catz._

object ForexClient {

  type ForexClient = Has[ForexClient.Service]

  trait Service {
    def retreiveRate(pair: Rate.Pair): IO[ForexError, Rate]
  }

  def live(client: Client[Task], maybeToken: Option[String]): Layer[Nothing, ForexClient] =
    ZLayer.succeed(new Service {

      lazy val textSink           = ConsoleSink.text(colored = true)
      lazy val izLogger: IzLogger = IzLogger(Trace, List(textSink))
      lazy val log: LogBIO[IO]    = LogstageZIO.withFiberId(izLogger)

      implicit def entityDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[Task, A] = jsonOf[Task, A]

      override def retreiveRate(pair: Rate.Pair): IO[ForexError, Rate] =
        performRequest[Rate](s"https://localhost:8081/rates?pairs=${pair.show}")

      def performRequest[T](uri: String)(implicit d: Decoder[T]): IO[ForexError, T] = {
        def call(uri: Uri): IO[ForexError, T] = {

          val headers = maybeToken match {
            case Some(token) => Headers.of(Header("token", token))
            case None        => Headers.empty
          }

          log.debug(s"Performing request with uri: $uri") *>
            client
              .expect[T](Request[Task](Method.GET, uri, headers = headers))
              .foldM(
                ex => log.error(s"Request error: ${ex.getMessage}") *> IO.fail(OneFrameLookupFailed(ex.getMessage)),
                response => log.debug(s"Response data: $response") *> ZIO.succeed(response)
              )
        }

        Uri
          .fromString(uri)
          .fold(_ => IO.fail(MalformedUrl(uri)), call)
      }
    })

  object factory {

    def retreiveRate(pair: Rate.Pair): ZIO[AppEnv, ForexError, Rate] =
      ZIO.accessM[ForexClient](_.get.retreiveRate(pair))
  }

}
