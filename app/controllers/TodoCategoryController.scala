package controllers

import model.TodoCategory
import model.forms.TodoCategoryCreatingForm.todoCategoryCreatingForm
import model.forms.TodoCategoryEditingForm.todoCategoryEditingForm
import model.forms.TodoCategoryEditingInput
import persistence.repository.TodoCategoryRepository
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TodoCategoryController @Inject() (
  mcc:                    MessagesControllerComponents,
  todoCategoryRepository: TodoCategoryRepository
) extends MessagesAbstractController(mcc) {

  def index() = Action.async {
    todoCategoryRepository.getAll map {
      todoCategories => Ok(views.html.todoCategory.Index(todoCategories))
    }
  }

  def createForm() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.todoCategory.Create(todoCategoryCreatingForm))
  }

  def create() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCategoryCreatingForm
      .bindFromRequest()
      .fold(
        formWithErrors =>
          Future.successful(BadRequest(
            views.html.todoCategory.Create(formWithErrors)
          )),
        todoCategoryCreatingInput => {
          val newTodoCategory: TodoCategory#WithNoId = TodoCategory(
            None,
            todoCategoryCreatingInput.name,
            todoCategoryCreatingInput.slug,
            todoCategoryCreatingInput.color
          ).toWithNoId

          todoCategoryRepository.insert(newTodoCategory).map(_ => Redirect(routes.TodoCategoryController.index()))

        }
      )
  }

  def editForm(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCategoryRepository.findById(TodoCategory.Id(id)) map {
      case None               => NotFound
      case Some(todoCategory) =>
        Ok(views.html.todoCategory.Edit(todoCategoryEditingForm.fill(TodoCategoryEditingInput(todoCategory)), todoCategory))
    }
  }

  def edit(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCategoryEditingForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          todoCategoryRepository.findById(TodoCategory.Id(id)) map {
            case None               => NotFound
            case Some(todoCategory) =>
              BadRequest(views.html.todoCategory.Edit(formWithErrors, todoCategory))
          }
        },
        todoCategoryEditingInput => {
          todoCategoryRepository.findById(TodoCategory.Id(id)) flatMap {
            case Some(todoCategory) =>
              val editedTodoCategory: TodoCategory#EmbeddedId = TodoCategory(
                Some(todoCategory.id),
                todoCategoryEditingInput.name,
                todoCategoryEditingInput.slug,
                todoCategoryEditingInput.color
              ).toEmbeddedId

              todoCategoryRepository
                .update(editedTodoCategory)
                .map(_ => Redirect(routes.TodoCategoryController.index()))
          }
        }
      )
  }

  def remove(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCategoryRepository.findById(TodoCategory.Id(id)) flatMap {
      case None               => Future.successful(NotFound)
      case Some(todoCategory) => todoCategoryRepository.delete(todoCategory).map(_ => Redirect(routes.TodoCategoryController.index()))
    }
  }

}
