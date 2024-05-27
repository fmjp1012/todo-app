/**
 * This is a sample of Todo Application.
 *
 */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

case class TodoCategory(
  id:        Option[TodoCategory.Id],
  name:      String,
  slug:      String,
  color:     TodoCategory.Color,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[TodoCategory.Id]

object TodoCategory {
  val  Id = the[Identity[Id]]
  type Id = Long @@ TodoCategory
  type WithNoId = Entity.WithNoId[Id, TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]

  sealed abstract class Color(val code: Short) extends EnumStatus
  object Color extends EnumStatus.Of[Color] {
    case object FRONTEND extends Color(code = 1)
    case object BACKEND  extends Color(code = 2)
    case object INFRA    extends Color(code = 3)
  }

  def categoryColorToCssClass(color: Color): String = color match {
    case Color.FRONTEND => "category-frontend"
    case Color.BACKEND  => "category-backend"
    case Color.INFRA    => "category-infra"
  }
}