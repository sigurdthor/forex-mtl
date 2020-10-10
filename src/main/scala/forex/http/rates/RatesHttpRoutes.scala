package forex.http
package rates

import cats.effect.Sync
import cats.syntax.flatMap._
import forex.Main.AppTask
import forex.programs.RatesProgram
import forex.programs.rates.{ Protocol => RatesProgramProtocol }
import forex.services.rates.errors.ForexError
import io.circe.Encoder
import org.http4s.{ EntityEncoder, HttpRoutes }
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import zio.Task
import zio.interop.catz._
import org.http4s.implicits._

class RatesHttpRoutes(rates: RatesProgram[AppTask]) {

  import Converters._, PathParams._, Protocol._

  object ioz extends Http4sDsl[AppTask]

  import ioz._

  implicit def entityEncoder[A](implicit encoder: Encoder[A]): EntityEncoder[Task, A] = jsonEncoderOf[Task, A]

  private[http] val prefixPath = "/rates"

  private val httpRoutes: HttpRoutes[AppTask] = HttpRoutes.of[AppTask] {
    case GET -> Root / "convert" / CurrencyVar(from) / "to" / CurrencyVar(to) =>
      rates
        .get(RatesProgramProtocol.GetRatesRequest(from, to))
        .foldM(
          {
            case e: ForexError => Ok(e.asInstanceOf[ForexError].asErrorResponse)
            case e: Throwable  => InternalServerError(e.getMessage)
          },
          rate => Ok(rate.asGetApiResponse)
        )
  }

  val routes: HttpRoutes[AppTask] = Router(
    prefixPath -> httpRoutes
  )

}
