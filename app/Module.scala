import com.google.inject.AbstractModule
import persistence.repository.impl.{TodoCategoryRepositoryImpl, TodoRepositoryImpl}
import persistence.repository.{TodoCategoryRepository, TodoRepository}

// Guiceモジュール
class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[TodoRepository]).to(classOf[TodoRepositoryImpl])
    bind(classOf[TodoCategoryRepository]).to(classOf[TodoCategoryRepositoryImpl])
  }
}
