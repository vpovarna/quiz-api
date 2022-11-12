package org.example.quiz.service

import cats.effect.IO
import org.example.quiz.dao.QuestionDao
import org.example.quiz.entities.QuestionEntity

class QuestionService(dao: QuestionDao) {

  def getQuestionById(questionId: Long): IO[Option[QuestionEntity]] = dao
    .getQuestionById(questionId)
    .map { maybeQuestion => maybeQuestion.map(QuestionEntity.fromRecord) }

  def deleteQuestion(questionId: Long): IO[Boolean] =
    dao.deleteQuestionById(questionId)

}
