/**
  * This is a sample of Todo Application.
  *
  */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

case class Todo(
  id:          Option[Todo.Id],
  category_id: Long,
  title:       String,
  body:        String,
  updatedAt:   LocalDateTime = NOW,
  createdAt:   LocalDateTime = NOW
) extends EntityModel[Todo.Id]

object Todo {
  val  Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId [Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]
}