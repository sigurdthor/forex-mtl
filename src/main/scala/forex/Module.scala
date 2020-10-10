package forex

import forex.Main.{ AppEnv, AppTask }
import forex.config.ApplicationConfig
import forex.http.rates.RatesHttpRoutes
import forex.programs._
import forex.services._
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.middleware.{ AutoSlash, Timeout }
import zio.interop.catz._

class Module(config: ApplicationConfig)(implicit runtime: zio.Runtime[AppEnv]) {

  private val ratesService: RatesService[AppTask] = RatesServices.dummy

  private val ratesProgram: RatesProgram[AppTask] = RatesProgram[AppTask](ratesService)

  private val ratesHttpRoutes: HttpRoutes[AppTask] = new RatesHttpRoutes(ratesProgram).routes

  type PartialMiddleware = HttpRoutes[AppTask] => HttpRoutes[AppTask]
  type TotalMiddleware   = HttpApp[AppTask] => HttpApp[AppTask]

  private val routesMiddleware: PartialMiddleware = {
    { http: HttpRoutes[AppTask] =>
      AutoSlash(http)
    }
  }

  private val appMiddleware: TotalMiddleware = { http: HttpApp[AppTask] =>
    Timeout(config.http.timeout)(http)
  }

  val httpApp: HttpApp[AppTask] = appMiddleware(routesMiddleware(ratesHttpRoutes).orNotFound)
}
