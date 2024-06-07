package model.forms

import com.google.inject.Inject
import model.{Todo, TodoCategory}
import persistence.repository.TodoCategoryRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class TodoCreatingInput(
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Short
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
      "categoryId" -> longNumber,
      "title"      -> nonEmptyText(maxLength = 30).verifying(titleConstraint),
      "body"       -> nonEmptyText(maxLength = 140),
      "state"      -> shortNumber.verifying(stateConstraint)
    )(TodoCreatingInput.apply)(TodoCreatingInput.unapply).verifying(
    )
  )
}
