package model.forms

import model.{Todo, TodoCategory}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class TodoEditingInput(
  categoryId: TodoCategory.Id,
  title:      String,
  body:       String,
  state:      Todo.Status
)

object TodoEditingInput {
  def apply(todo: Todo#EmbeddedId): TodoEditingInput =
    new TodoEditingInput(todo.v.categoryId, todo.v.title, todo.v.body, todo.v.state)
}

object TodoEditingForm {

  val titleConstraint: Constraint[String] = Constraint("constraints.titleCheck") { title =>
    if (title.contains("\n") || title.contains("\r")) {
      Invalid(ValidationError("タイトルに改行を含むことはできません。"))
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

  val stateConstraint: Constraint[Short] = Constraint("constraints.stateCheck") { state =>
    Todo.Status.find(_.code == state) match {
      case None    => Invalid(ValidationError("Invalid state"))
      case Some(_) => Valid
    }
  }

  val todoEditingForm: Form[TodoEditingInput] = Form(
    mapping(
      "categoryId" -> longNumber.transform(TodoCategory.Id(_), (id: TodoCategory.Id) => id),
      "title"      -> nonEmptyText(maxLength = 30).verifying(titleConstraint, alphaNumericConstraint),
      "body"       -> nonEmptyText(maxLength = 140),
      "state"      -> shortNumber.verifying(stateConstraint).transform(Todo.Status(_), (state: Todo.Status) => state.code)
    )(TodoEditingInput.apply)(TodoEditingInput.unapply)
  )
}
