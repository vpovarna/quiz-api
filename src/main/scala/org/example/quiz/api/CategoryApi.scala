package org.example.quiz.api

import cats.effect.IO
import org.example.quiz.entities.{CategoryEntity, CategoryName}
import org.example.quiz.service.CategoryService
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class CategoryApi(categoryService: CategoryService) extends Http4sDsl[IO] {
  private implicit val categoriesEncoder
      : EntityEncoder[IO, Seq[CategoryEntity]] =
    jsonEncoderOf[IO, Seq[CategoryEntity]]
  private implicit val categoryEncoder: EntityEncoder[IO, CategoryEntity] =
    jsonEncoderOf[IO, CategoryEntity]

  private implicit val categoryNameDecoder: EntityDecoder[IO, CategoryName] =
    jsonOf[IO, CategoryName]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / LongVar(categoryId) =>
      categoryService.get(categoryId).flatMap {
        case Some(category) => Ok(category)
        case None           => NotFound(s"Category $categoryId does not exist")
      }
    case GET -> Root => Ok(categoryService.all)
    case request @ POST -> Root =>
      val response = for {
        categoryName <- request.as[CategoryName]
        categoryId <- categoryService.addCategory(categoryName)
      } yield categoryId
      Ok(response)

    case DELETE -> Root / LongVar(categoryId) =>
      categoryService
        .deleteCategory(categoryId)
        .flatMap(status =>
          if (status)
            Ok(s"Category id: $categoryId has been deleted successfully!")
          else
            Ok(s"Unable to delete category id: $categoryId")
        )
  }

}
