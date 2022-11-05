package org.example.quiz.dao

import scala.concurrent.{ExecutionContext, Future}

import io.getquill.{PostgresJAsyncContext, SnakeCase}
import org.example.quiz.dao.records.{Answer, Question}

trait QuestionAnswerDao {
  def save(
      newQuestion: Question,
      newAnswer: Seq[Answer]
  ): Future[(Long, Seq[Long])]
  def pickByCategoryId(
      categoryId: Long,
      n: Int
  ): Future[Map[Question, Seq[Answer]]]
  def deleteById(id: Long): Future[Boolean]
  def getCorrectQuestionAnswer(questionId: Seq[Long]): Future[Seq[(Long, Long)]]
}

class QuestionAnswerDaoImpl(ctx: PostgresJAsyncContext[SnakeCase.type])(implicit
    ec: ExecutionContext
) extends QuestionAnswerDao {
  import ctx._
  private val questions = quote { query[Question] }
  private val answers = quote { query[Answer] }

  override def save(
      newQuestion: Question,
      newAnswers: Seq[Answer]
  ): Future[(Long, Seq[Long])] = {
    val saveQuestion = quote {
      questions.insert(lift(newQuestion)).returningGenerated(_.id)
    }
    val saveAnswers = { questionId: Long =>
      val newAnswersWithQuestionId =
        newAnswers.map(_.copy(questionId = questionId))
      quote {
        liftQuery(newAnswersWithQuestionId).foreach { a =>
          answers.insert(a).returningGenerated(_.id)
        }
      }
    }

    transaction { implicit ec =>
      for {
        questionId <- run(saveQuestion)
        answerId <- run(saveAnswers(questionId))
      } yield questionId -> answerId
    }

  }

  override def pickByCategoryId(
      categoryId: Long,
      n: Int
  ): Future[Map[Question, Seq[Answer]]] = {
    val result = run {
      for {
        question <- questions
          .filter(_.categoryId == lift(categoryId))
          .sortBy(_ => infix"random()")
          .take(lift(n))
        answer <- answers.filter(_.questionId == question.id)
      } yield question -> answer
    }

    result.map { questionsAnswers =>
      val questions: Seq[Question] =
        questionsAnswers.map { case (q, _) => q }.distinct
      val answersByQuestionId: Map[Long, Seq[Answer]] =
        questionsAnswers.map { case (_, a) => a }.groupBy(_.questionId)
      questions.map { question =>
        question -> answersByQuestionId.getOrElse(question.id, Seq.empty)
      }.toMap
    }
  }

  override def deleteById(id: Long): Future[Boolean] = {
    val q = quote { questions.filter(_.id == lift(id)).delete }
    run(q).map(_ > 0)
  }

  override def getCorrectQuestionAnswer(
      questionId: Seq[Long]
  ): Future[Seq[(Long, Long)]] = {
    val q = quote {
      for {
        question <- questions.filter(q => lift(questionId).contains(q.id))
        correctAnswer <- answers.filter(a =>
          a.questionId == question.id && a.isCorrect
        )
      } yield question.id -> correctAnswer.id
    }

    run(q)
  }
}
