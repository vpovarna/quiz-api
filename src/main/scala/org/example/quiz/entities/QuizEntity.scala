package org.example.quiz.entities

import io.circe._
import io.circe.generic.semiauto._
import org.example.quiz.dao.records.{Answer, Question}

case class QuizEntity(questions: List[QuestionEntity])

object QuizEntity {
  implicit val encoder: Encoder[QuizEntity] =
    deriveEncoder[QuizEntity]
  implicit val decoder: Decoder[QuizEntity] =
    deriveDecoder[QuizEntity]
}

case class QuestionEntity(
    id: Long,
    text: String
)
object QuestionEntity {

  implicit val encoder: Encoder[QuestionEntity] =
    deriveEncoder[QuestionEntity]
  implicit val decoder: Decoder[QuestionEntity] =
    deriveDecoder[QuestionEntity]

  def fromRecord(question: Question): QuestionEntity =
    apply(question.id, question.text)

}

case class AnswerEntity(id: Long, text: String, isCorrect: Boolean)

object AnswerEntity {

  implicit val encoder: Encoder[AnswerEntity] =
    deriveEncoder[AnswerEntity]
  implicit val decoder: Decoder[AnswerEntity] =
    deriveDecoder[AnswerEntity]

  def fromRecord(answer: Answer): AnswerEntity =
    apply(answer.id, answer.text, answer.isCorrect)
}
