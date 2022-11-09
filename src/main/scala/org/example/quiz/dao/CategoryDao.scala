package org.example.quiz.dao

import cats.effect._
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.example.quiz.dao.records.Category
import org.example.quiz.entities.CategoryName

class CategoryDao(xa: Transactor[IO]) {

  def save(categoryName: CategoryName): IO[Long] = {
    val update =
      sql"insert into category (name) values (${categoryName.name})".update
    val action = update.withUniqueGeneratedKeys[Long]("id")
    action.transact(xa)
  }

  def getAll: IO[Seq[Category]] = {
    val query = sql"select * from category".query[Category]
    query.stream.compile.toList.transact(xa)
  }

  def findById(id: Long): IO[Option[Category]] = {
    val query =
      sql"select * from category where id=$id".query[Category]
    val action = query.option
    action.transact(xa)
  }

  def deleteById(id: Long): IO[Boolean] = {
    val update = sql"delete from category where id = $id".update
    update.run.transact(xa).map(_ == 1)
  }
}
