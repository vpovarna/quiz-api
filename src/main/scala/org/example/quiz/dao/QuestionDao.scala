package org.example.quiz.dao

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.example.quiz.dao.records.Question

class QuestionDao(xa: Transactor[IO]) {

  /** @param questionId
    *   \= Question Id
    * @return
    *   Return a question for the specified questionId
    */
  def getQuestionById(questionId: Long): IO[Option[Question]] = {
    val q =
      sql"select * from question where id = $questionId"
        .query[(Long, Long, String)]
        .map { case (id, categoryId, text) => Question(id, text, categoryId) }
        .option
    q.transact(xa)
  }

  /** @param id
    *   \= Question id
    * @return
    *   Frue if the question has been deleted successfully. False if the
    *   question can't be deleted.
    */
  def deleteQuestionById(id: Long): IO[Boolean] = {
    val query = sql"delete from question where id = $id".update
    query.run.transact(xa).map(_ == 1)
  }
}
