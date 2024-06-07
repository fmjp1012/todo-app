package persistence.repository.impl

import com.zaxxer.hikari.HikariDataSource
import ixias.slick.SlickRepository
import ixias.slick.builder.{DatabaseBuilder, HikariConfigBuilder}
import ixias.slick.jdbc.MySQLProfile.api._
import ixias.slick.model.DataSourceName
import model.TodoCategory
import persistence.db.TodoCategoryTable
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

  private val todoCategoryTable = TableQuery[TodoCategoryTable]

  def getAll: Future[Seq[TodoCategory]] = {
    slave.run(todoCategoryTable.result)
  }

  def findById(id: TodoCategory.Id): Future[Option[TodoCategory.EmbeddedId]] = {
    slave
      .run(
        todoCategoryTable
          .filter(_.id === id)
          .result
          .headOption
      )
      .map(_.map(_.toEmbeddedId))
  }
}
