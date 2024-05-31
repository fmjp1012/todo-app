/**
 * This is a sample of Todo Application.
 *
 */

package lib.persistence

import com.zaxxer.hikari.HikariDataSource
import ixias.slick.SlickRepository
import ixias.slick.builder.{DatabaseBuilder, HikariConfigBuilder}
import ixias.slick.jdbc.MySQLProfile.api._
import ixias.slick.model.DataSourceName
import lib.model.{Todo, TodoCategory}
import lib.persistence.db.{TodoCategoryTable, TodoTable}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoRepository @Inject()()(implicit val ec: ExecutionContext) extends SlickRepository[Todo.Id, Todo] {
  val master: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder.default(
        DataSourceName("ixias.db.mysql://master/to_do")
      ).build()
    )
  )

  val slave: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder.default(
        DataSourceName("ixias.db.mysql://slave/to_do")
      ).build()
    )
  )

  val todoTable = TableQuery[TodoTable]
  val todoCategoryTable = TableQuery[TodoCategoryTable]

  def getAll: Future[Seq[(Todo, TodoCategory)]] = {
    slave.run(
      {
        for {
          todo <- todoTable
          todoCategory <- todoCategoryTable if todo.categoryId === todoCategory.id
        } yield (todo, todoCategory)
      }.result
    )
  }


}