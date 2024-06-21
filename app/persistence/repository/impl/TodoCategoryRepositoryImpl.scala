package persistence.repository.impl

import com.zaxxer.hikari.HikariDataSource
import ixias.slick.SlickRepository
import ixias.slick.builder.{DatabaseBuilder, HikariConfigBuilder}
import ixias.slick.jdbc.MySQLProfile.api._
import ixias.slick.model.DataSourceName
import model.TodoCategory
import persistence.db.{TodoCategoryTable, TodoTable}
import persistence.repository.TodoCategoryRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoCategoryRepositoryImpl @Inject() ()(implicit val ec: ExecutionContext)
  extends SlickRepository[TodoCategory.Id, TodoCategory]
    with TodoCategoryRepository {
  private val master: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder
        .default(
          DataSourceName("ixias.db.mysql://master/to_do")
        )
        .build()
    )
  )

  private val slave: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder
        .default(
          DataSourceName("ixias.db.mysql://slave/to_do")
        )
        .build()
    )
  )

  private val todoTable         = TableQuery[TodoTable]
  private val todoCategoryTable = TableQuery[TodoCategoryTable]

  def getAll: Future[Seq[TodoCategory#EmbeddedId]] = {
    slave.run(
      todoCategoryTable.result
    ).map(_.map(_.toEmbeddedId))
  }

  def findById(id: TodoCategory.Id): Future[Option[TodoCategory.EmbeddedId]] = {
    slave.run(
      todoCategoryTable
        .filter(_.id === id)
        .result
        .headOption
    )
      .map(_.map(_.toEmbeddedId))
  }

  def insert(newTodoCategory: TodoCategory#WithNoId) = {
    master.run(
      todoCategoryTable returning todoCategoryTable.map(_.id) += newTodoCategory.v
    )
  }

  def update(editedTodoCategory: TodoCategory#EmbeddedId) = {
    master.run(
      todoCategoryTable
        .filter(_.id === editedTodoCategory.id)
        .update(editedTodoCategory.v)
        .map(TodoCategory.Id(_))
    )
  }

  def delete(todoCategory: TodoCategory#EmbeddedId): Future[TodoCategory.Id] = {
    master.run(
      (
        for {
          id <- todoCategoryTable.filter(_.id === todoCategory.id).delete
          _  <- todoTable.filter(_.categoryId === todoCategory.id).delete
        } yield TodoCategory.Id(id)
      ).transactionally
    )
  }
}
