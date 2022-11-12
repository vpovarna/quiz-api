package org.example.quiz.api

import cats.effect.IO
import org.example.quiz.entities.QuestionEntity
import org.example.quiz.service.QuestionService
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class QuestionApi(quizService: QuestionService) extends Http4sDsl[IO] {

  implicit val questionEncoder: EntityEncoder[IO, QuestionEntity] =
    jsonEncoderOf[IO, QuestionEntity]
  implicit val questionDecoder: EntityDecoder[IO, QuestionEntity] =
    jsonOf[IO, QuestionEntity]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / LongVar(categoryId) =>
      quizService.getQuestionById(categoryId).flatMap {
        case Some(question) => Ok(question)
        case None           => NotFound(s"No question found for $categoryId")
      }

    case DELETE -> Root / LongVar(questionId) =>
      quizService.deleteQuestion(questionId).flatMap { status =>
        if (status)
          Ok(s"Question id: $questionId has been deleted successfully!")
        else
          Ok(s"Unable to delete question id: $questionId")
      }
  }

}
