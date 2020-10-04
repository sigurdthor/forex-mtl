package forex

import cats.effect.Resource
import forex.config._
import forex.services.rates.clients.ForexClient
import forex.services.rates.errors.ForexError
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze._
import zio.clock.Clock
import zio.console.Console
import zio._
import zio.interop.catz._
import forex.services.rates.clients.ForexClient.ForexClient

import scala.concurrent.ExecutionContext.Implicits

object Main extends zio.App {

  type AppEnv = Console with Clock with ForexClient
  type AppTask[A] = RIO[AppEnv, A]

  def run(args: List[String]) = server.exitCode

  val server = for {
    maybeToken <- system.env("FOREX_TOKEN")
    config <- ConfigFactory.load("app")
    module = new Module[AppTask](config)
    client <- buildHttpClient
    _ <- buildHttpServer(module, config, client, maybeToken)
  } yield ()

  private def buildHttpClient: RIO[ZEnv, Resource[Task, Client[Task]]] =
    ZIO
      .runtime[ZEnv]
      .map { implicit rts =>
        BlazeClientBuilder
          .apply[Task](Implicits.global)
          .resource
      }

  def buildHttpServer(module: Module[AppTask],
                      config: ApplicationConfig,
                      http4sClient: Resource[Task, Client[Task]],
                      maybeAuthToken: Option[String]) =
    http4sClient.use { httpClient =>
      ZIO
        .runtime[AppEnv]
        .flatMap { implicit rts =>
          BlazeServerBuilder[AppTask]
            .bindHttp(config.http.port, config.http.host)
            .withHttpApp(module.httpApp)
            .serve
            .compile
            .drain
        }
        .provideCustomLayer(Console.live ++ Clock.live ++ ForexClient.live(httpClient, maybeAuthToken))
    }
}
