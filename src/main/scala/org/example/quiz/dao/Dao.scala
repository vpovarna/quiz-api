package org.example.quiz.dao

import cats.effect.IO
import doobie.util.transactor.Transactor

class Dao() {
  // TODO: read user, pass, hostname and dbName from env variables
  private val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/quizdb",
    "docker",
    "docker"
  )
  val generic = new GenericDao(xa)
  val category: CategoryDao = new CategoryDao(xa)
  val questionAnswerDao: QuestionAnswerDao = new QuestionAnswerDao(xa)
  val questionDao: QuestionDao = new QuestionDao(xa)
  val answerDao: AnswerDao = new AnswerDao(xa)
}
