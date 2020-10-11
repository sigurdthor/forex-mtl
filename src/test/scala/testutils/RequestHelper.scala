package testutils

import org.http4s._
import zio.RIO
import zio.interop.catz._
import zio.test.Assertion.{equalTo, isEmpty}
import zio.test.{assert, assertM, TestResult}

object RequestHelper {

  def request[F[_]](method: Method, uri: String): Request[F] =
    Request(method = method, uri = Uri.fromString(uri).toOption.get)

  def checkRequest[R, A](actual: RIO[R, Response[RIO[R, *]]], expectedStatus: Status, expectedBody: Option[A])(
      implicit
      ev: EntityDecoder[RIO[R, *], A]
  ): RIO[R, TestResult] =
    for {
      actual <- actual
      bodyResult <- expectedBody.fold(
          assert(actual.bodyAsText.compile.toVector, isEmpty)
        )(expected => assert(actual.as[A], equalTo(expected)))
      statusResult = assert(actual.status, equalTo(expectedStatus))
    } yield bodyResult && statusResult
}
