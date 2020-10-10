package testenv

import forex.Main.{AppEnv, AppTask}
import forex.domain.{Price, Rate, Timestamp}
import forex.services.rates.clients.ForexClient.{ForexClient, Service}
import forex.services.rates.errors
import zio.clock.Clock
import zio.console.Console
import zio.{IO, Layer, ZIO, ZLayer}

object TestEnvironment {

  def forexClientTest: Layer[Nothing, ForexClient] =
    ZLayer.succeed((pair: Rate.Pair) => IO.succeed(Rate(pair, Price(BigDecimal(100)), Timestamp.now)))

  def withEnv[A](task: AppTask[A]) =
    ZIO.environment[AppEnv].provideCustomLayer(Console.live ++ Clock.live ++ forexClientTest) >>> task
}


