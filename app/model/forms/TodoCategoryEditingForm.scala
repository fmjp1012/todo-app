package model.forms

import model.TodoCategory
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class TodoCategoryEditingInput(
  name:  String,
  slug:  String,
  color: TodoCategory.Color,
)

object TodoCategoryEditingInput {
  def apply(todoCategory: TodoCategory#EmbeddedId): TodoCategoryEditingInput =
    new TodoCategoryEditingInput(todoCategory.v.name, todoCategory.v.slug, todoCategory.v.color)
}

object TodoCategoryEditingForm {

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

  val todoCategoryEditingForm: Form[TodoCategoryEditingInput] = Form(
    mapping(
      "name"  -> nonEmptyText(maxLength = 30).verifying(nameConstraint),
      "slug"  -> nonEmptyText(maxLength = 30).verifying(slugConstraint),
      "color" -> shortNumber.transform(TodoCategory.Color(_), (color: TodoCategory.Color) => color.code),
    )(TodoCategoryEditingInput.apply)(TodoCategoryEditingInput.unapply)
  )
}
