package service

import javax.inject.Inject
import lib.persistence.TodoRepository

class TodoService @Inject()(todoRepository: TodoRepository) {

}
