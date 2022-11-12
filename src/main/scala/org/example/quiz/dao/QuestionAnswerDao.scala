package org.example.quiz.dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.{Read, Write}
import org.example.quiz.dao.records.{Answer, Category, Question}

case class QuestionAnswerDao(xa: Transactor[IO]) {

  implicit val questionWrite: Write[Question] =
    Write[(Long, String, Long)].contramap { question =>
      (question.id, question.text, question.categoryId)
    }

  implicit val questionRead: Read[Question] =
    Read[(Long, Long, String)].map { case (id, categoryId, text) =>
      Question(id, text, categoryId)
    }

  implicit val answerWrite: Write[Answer] =
    Write[(Long, Long, String, Boolean)].contramap { answer =>
      (answer.id, answer.questionId, answer.text, answer.isCorrect)
    }

  implicit val answerRead: Read[Answer] =
    Read[(Long, Long, String, Boolean)].map {
      case (id, questionId, text, isCorrect) =>
        Answer(id, questionId, text, isCorrect)
    }

  implicit val categoryRead: Read[Category] =
    Read[(Long, String)].map { case (id, name) =>
      Category(id, name)
    }

  /** @param newQuestion
    *   \= new question
    * @param newAnswer
    *   \= new answer
    * @return
    *   returns a tuple containing the id of the inserted question with the id
    *   of the answer
    */
  def save(newQuestion: Question, newAnswer: Answer): IO[(Long, Long)] = {
    def getCategory(categoryId: Long) =
      sql"select * from category where id=$categoryId"
        .query[(Long, String)]
        .option

    def insertQuestion(categoryId: Long) =
      sql"insert into question(category_id, text) values ($categoryId, ${newQuestion.text})".update
        .withUniqueGeneratedKeys[Long]("id")

    def insertAnswer(questionId: Long) =
      sql"insert into answer (question_id, text) values ($questionId, ${newAnswer.text})".update
        .withUniqueGeneratedKeys[Long]("id")

    // This is done inside a transaction
    val query = for {
      someCategory <- getCategory(newQuestion.categoryId)
      questionId <- someCategory match {
        case Some((categoryId, _)) => insertQuestion(categoryId)
        case None                  => 0L.pure[ConnectionIO]
      }
      answerId <- insertAnswer(questionId)
    } yield questionId -> answerId

    query.transact(xa)
  }

  /** @param questionId
    *   \= Question Id
    * @return
    *   the list of correct answers for the provided questionId
    */
  def getCorrectQuestionAnswersId(questionId: Long): IO[(Long, List[Long])] = {

    def getQuestion: doobie.ConnectionIO[Option[(Long, Long, String)]] =
      sql"select * from question where id = $questionId"
        .query[(Long, Long, String)]
        .option

    def getAnswersByQuestionId(
        questionId: Long
    ): doobie.ConnectionIO[List[(Long, Long, String, Boolean)]] =
      sql"select * from answer where question_id=$questionId"
        .query[(Long, Long, String, Boolean)]
        .to[List]

    val query =
      for {
        someQuestion <- getQuestion
        answers <- someQuestion match {
          case Some((id, _, _)) => getAnswersByQuestionId(id)
          case None =>
            List.empty[(Long, Long, String, Boolean)].pure[ConnectionIO]
        }
      } yield questionId -> answers.filter(_._4).map(_._1)

    query.transact(xa)
  }
}
