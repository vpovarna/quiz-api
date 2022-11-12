package org.example.quiz.service

import cats.effect.IO
import org.example.quiz.dao.AnswerDao
import org.example.quiz.entities.AnswerEntity

class AnswerService(dao: AnswerDao) {

  def getAnswersByQuestionId(questionId: Long): IO[Seq[AnswerEntity]] =
    dao.getAnswersByQuestionId(questionId).map { answers =>
      answers.map(AnswerEntity.fromRecord)
    }
}
