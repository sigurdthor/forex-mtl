import forex.Main.AppTask
import forex.http.rates.RatesHttpRoutes
import forex.programs.RatesProgram
import forex.services.{RatesService, RatesServices}
import org.http4s.{Status, _}
import testenv.TestEnvironment.withEnv
import testutils.RequestHelper.{checkRequest, request}
import zio.test.{DefaultRunnableSpec, suite, testM}

object ForexServiceSpec
    extends DefaultRunnableSpec(
      suite("routes suite")(
        testM("rate conversion request") {
          withEnv {
            val req = request[AppTask](Method.GET, "/rates/convert/USD/to/EUR")
            val ratesService: RatesService[AppTask] = RatesServices.dummy

            val ratesProgram: RatesProgram[AppTask] = RatesProgram[AppTask](ratesService)

            val ratesHttpRoutes: HttpRoutes[AppTask] = new RatesHttpRoutes[AppTask](ratesProgram).routes

            checkRequest(
              ratesHttpRoutes.run(req),
              Status.Ok,
              Some(json"""
           [

           ]
                """)
            )
          }
        }
      )
    )
