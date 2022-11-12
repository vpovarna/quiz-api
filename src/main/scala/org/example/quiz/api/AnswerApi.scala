package org.example.quiz.api

import cats.effect.IO
import org.example.quiz.entities.AnswerEntity
import org.example.quiz.service.AnswerService
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityEncoder, HttpRoutes}

class AnswerApi(answersService: AnswerService) extends Http4sDsl[IO] {

  implicit val questionEncoder: EntityEncoder[IO, Seq[AnswerEntity]] =
    jsonEncoderOf[IO, Seq[AnswerEntity]]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / LongVar(questionId) =>
      Ok(answersService.getAnswersByQuestionId(questionId))
  }

}
