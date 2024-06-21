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
      Invalid(ValidationError("nameに改行を含むことはできません。"))
    } else {
      Valid
    }
  }

  val slugConstraint: Constraint[String] = Constraint("constraints.slugCheck") { slug =>
    if (slug.contains("\n") || slug.contains("\r")) {
      Invalid(ValidationError("slugに改行を含むことはできません。"))
    } else {
      Valid
    }
  }

  val alphaNumericConstraint: Constraint[String] = Constraint("constraints.alphaNumericCheck") { string =>
    if (!string.matches("[a-zA-Z0-9]+")) {
      Invalid(ValidationError("英数字で入力する必要があります。"))
    } else {
      Valid
    }
  }

  val colorConstraints: Constraint[Short] = Constraint("constraints.colorCheck") { color =>
    TodoCategory.Color.find(_.code == color) match {
      case None    => Invalid(ValidationError("Invalid color"))
      case Some(_) => Valid
    }
  }

  val todoCategoryCreatingForm: Form[TodoCategoryCreatingInput] = Form(
    mapping(
      "name"  -> nonEmptyText(maxLength = 30).verifying(nameConstraint),
      "slug"  -> nonEmptyText(maxLength = 30).verifying(slugConstraint, alphaNumericConstraint),
      "color" -> shortNumber.verifying(colorConstraints).transform(TodoCategory.Color(_), (color: TodoCategory.Color) => color.code),
    )(TodoCategoryCreatingInput.apply)(TodoCategoryCreatingInput.unapply)
  )
}
