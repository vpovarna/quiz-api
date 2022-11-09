package org.example.quiz

import scala.concurrent.ExecutionContext

import cats.effect.{ExitCode, IO, IOApp}
import org.example.quiz.api.Api
import org.example.quiz.dao.Dao
import org.example.quiz.service.Services
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  private val dao = new Dao()
  private val services = new Services(dao)
  private val api = new Api(services)

  private val httpApp = Router(
    "/" -> api.generic.routes,
    "categories" -> api.categories.routes
  ).orNotFound

  private def stream(args: List[String]): fs2.Stream[IO, ExitCode] =
    BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(8000, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve

  override def run(args: List[String]): IO[ExitCode] = stream(
    args
  ).compile.drain.as(ExitCode.Success)
}
