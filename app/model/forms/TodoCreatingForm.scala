package model.forms

import model.{Todo, TodoCategory}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class TodoCreatingInput(
  categoryId: TodoCategory.Id,
  title:      String,
  body:       String,
  state:      Todo.Status
)

object TodoCreatingForm {

  val titleConstraint: Constraint[String] = Constraint("constraints.titleCheck") { title =>
    if (title.contains("\n") || title.contains("\r")) {
      Invalid(ValidationError("タイトルに改行を含むことはできません。"))
    } else {
      Valid
    }
  }

  val stateConstraint: Constraint[Short] = Constraint("constraints.stateCheck") { state =>
    if (state != Todo.Status.IS_INACTIVE.code) {
      Invalid(ValidationError("Invalid State"))
    } else {
      Valid
    }
  }

  val todoCreatingForm: Form[TodoCreatingInput] = Form(
    mapping(
      "categoryId" -> longNumber.transform(TodoCategory.Id(_), (id: TodoCategory.Id) => id),
      "title"      -> nonEmptyText(maxLength = 30).verifying(titleConstraint),
      "body"       -> nonEmptyText(maxLength = 140),
      "state"      -> shortNumber.verifying(stateConstraint).transform(Todo.Status(_), (state: Todo.Status) => state.code)
    )(TodoCreatingInput.apply)(TodoCreatingInput.unapply)
  )
}
