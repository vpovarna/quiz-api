package org.example.quiz.dao

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.example.quiz.dao.records.Answer

class AnswerDao(xa: Transactor[IO]) {

  /** @param questionId
    *   \= Question Id
    * @return
    *   the list of answers for the specified questionId
    */
  def getAnswersByQuestionId(questionId: Long): IO[List[Answer]] = {
    val q =
      sql"select * from answer where question_id=$questionId"
        .query[Answer]
        .to[List]
    q.transact(xa)
  }
}
