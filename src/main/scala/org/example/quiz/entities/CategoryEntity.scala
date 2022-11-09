package org.example.quiz.entities

import io.circe._
import io.circe.generic.semiauto._
import org.example.quiz.dao.records.Category

case class CategoryName(name: String)
case class CategoryEntity(id: Long, categoryName: CategoryName)

object CategoryName {
  implicit val encoder: Encoder[CategoryName] =
    deriveEncoder[CategoryName]
  implicit val decoder: Decoder[CategoryName] =
    deriveDecoder[CategoryName]
}

object CategoryEntity {
  implicit val encoder: Encoder[CategoryEntity] =
    deriveEncoder[CategoryEntity]
  implicit val decoder: Decoder[CategoryEntity] =
    deriveDecoder[CategoryEntity]

  def fromRecord(record: Category): CategoryEntity =
    apply(id = record.id, categoryName = CategoryName(record.name))
}
