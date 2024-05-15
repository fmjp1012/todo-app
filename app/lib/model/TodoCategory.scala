/**
 * This is a sample of Todo Application.
 *
 */

package lib.model

import ixias.model._

import java.time.LocalDateTime

case class TodoCategory(
  id:        Option[TodoCategory.Id],
  name:      String,
  slug:      String,
  color:     Short,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[TodoCategory.Id]

object TodoCategory {
  val  Id = the[Identity[Id]]
  type Id = Long @@ TodoCategory
  type WithNoId = Entity.WithNoId[Id, TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]
}