package model.forms

import model.TodoCategory
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class TodoCategoryCreatingInput(
  name:  String,
  slug:  String,
  color: TodoCategory.Color,
)

object TodoCategoryCreatingForm {

  val nameConstraint: Constraint[String] = Constraint("constraints.nameCheck") { name =>
    if (name.contains("\n") || name.contains("\r")) {
      Invalid(ValidationError("タイトルに改行を含むことはできません。"))
    } else {
      Valid
    }
  }

  val slugConstraint: Constraint[String] = Constraint("constraints.slugCheck") { slug =>
    if (slug.contains("\n") || slug.contains("\r")) {
      Invalid(ValidationError("タイトルに改行を含むことはできません。"))
    } else {
      Valid
    }
  }

  val todoCategoryCreatingForm: Form[TodoCategoryCreatingInput] = Form(
    mapping(
      "name"  -> nonEmptyText(maxLength = 30).verifying(nameConstraint),
      "slug"  -> nonEmptyText(maxLength = 30).verifying(slugConstraint),
      "color" -> shortNumber.transform(TodoCategory.Color(_), (color: TodoCategory.Color) => color.code),
    )(TodoCategoryCreatingInput.apply)(TodoCategoryCreatingInput.unapply)
  )
}
