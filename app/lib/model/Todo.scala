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
  category_id: TodoCategory.Id,
  title:       String,
  body:        String,
  state:       Todo.Status,
  updatedAt:   LocalDateTime = NOW,
  createdAt:   LocalDateTime = NOW
) extends EntityModel[Todo.Id]

object Todo {
  val  Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId [Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object IS_INACTIVE  extends Status(code = 0, name = "TODO")
    case object IS_ACTIVE    extends Status(code = 1, name = "進行中")
    case object IS_CONPLETED extends Status(code = 2, name = "完了")
  }
}