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
import lib.model.Todo
import lib.persistence.db.TodoTable

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoRepository @Inject() ()(implicit val ec: ExecutionContext) extends SlickRepository[Todo.Id, Todo] {
  val master: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(
      HikariConfigBuilder.default(
        DataSourceName("ixias.db.mysql://master/to_do")
      ).build()
    )
  )
  val slave: Database = DatabaseBuilder.fromHikariDataSource(
    new HikariDataSource(HikariConfigBuilder.default(DataSourceName("ixias.db.mysql://slave/to_do")).build())
  )

  val todoTable = TableQuery[TodoTable]

  /**
    * Get User Data
    */
  def get(): Future[Seq[TodoTable#TableElementType]] = {
    slave.run(todoTable.result)
  }

  /**
    * Add User Data
   */

  /**
   * Update User Data
   */

  /**
   * Delete User Data
   */
}