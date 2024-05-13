/**
  * This is a sample of Todo Application.
  *
  */

package lib.persistence.db

import java.time.LocalDateTime
import ixias.slick.jdbc.MySQLProfile.api._
import lib.model.Todo

case class TodoTable(tag: Tag) extends Table[Todo](tag, "to_do") {
    // Columns
    /* @1 */ def id          = column[Todo.Id]       ("id",          UInt64, O.PrimaryKey, O.AutoInc)
    /* @2 */ def category_id = column[Long]          ("category_id", UInt64)
    /* @3 */ def title       = column[String]        ("title",       Utf8Char255)
    /* @4 */ def body        = column[String]        ("body",        Text)
    /* @5 */ def updatedAt   = column[LocalDateTime] ("updated_at",  TsCurrent)
    /* @6 */ def createdAt   = column[LocalDateTime] ("created_at",  Ts)

    // DB <=> Scala の相互のmapping定義
    def * = (id.?, category_id, title, body, updatedAt, createdAt) <> (
      (Todo.apply _).tupled,
      (Todo.unapply _).andThen(_.map(_.copy(
        _5 = LocalDateTime.now()
      )))
    )
}