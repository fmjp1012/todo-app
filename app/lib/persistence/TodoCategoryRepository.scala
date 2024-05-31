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
import lib.model.TodoCategory
import lib.persistence.db.TodoCategoryTable

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoCategoryRepository @Inject() ()(implicit val ec: ExecutionContext) extends SlickRepository[TodoCategory.Id, TodoCategory] {
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

  val todoTable = TableQuery[TodoCategoryTable]
}