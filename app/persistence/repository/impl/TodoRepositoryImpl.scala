package persistence.repository.impl

import com.zaxxer.hikari.HikariDataSource
import ixias.slick.SlickRepository
import ixias.slick.builder.{DatabaseBuilder, HikariConfigBuilder}
import ixias.slick.jdbc.MySQLProfile.api._
import ixias.slick.model.DataSourceName
import model.{Todo, TodoCategory}
import persistence.db.{TodoCategoryTable, TodoTable}
import persistence.repository.TodoRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoRepositoryImpl @Inject() ()(implicit val ec: ExecutionContext)
    extends SlickRepository[Todo.Id, Todo]
    with TodoRepository {
  val master: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder
        .default(
          DataSourceName("ixias.db.mysql://master/to_do")
        )
        .build()
    )
  )

  val slave: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder
        .default(
          DataSourceName("ixias.db.mysql://slave/to_do")
        )
        .build()
    )
  )

  val todoTable         = TableQuery[TodoTable]
  val todoCategoryTable = TableQuery[TodoCategoryTable]

  def getAll: Future[Seq[(Todo#EmbeddedId, TodoCategory#EmbeddedId)]] = {
    slave
      .run(
        {
          for {
            todo         <- todoTable
            todoCategory <- todoCategoryTable if todo.categoryId === todoCategory.id
          } yield (todo, todoCategory)
        }.result
      )
      .map(_.map { case (todo, todoCategory) =>
        (todo.toEmbeddedId, todoCategory.toEmbeddedId)
      })

  }

  def insert(newTodo: Todo#WithNoId): Future[Todo.Id] = {
    master.run(todoTable returning todoTable.map(_.id) += newTodo)
  }
}
