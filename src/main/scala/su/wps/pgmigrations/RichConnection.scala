package su.wps.pgmigrations

import java.sql.{Connection, PreparedStatement}

object RichConnection {
  implicit def connectionToRichConnection(c: Connection): RichConnection =
    new RichConnection(c)
}

/**
  * A rich Connection class that provides a withPreparedStatement()
  * method.
  */
class RichConnection(self: Connection) {
  def withPreparedStatement[T](sql: String)(f: PreparedStatement => T): T =
    With.autoClosingStatement(self.prepareStatement(sql))(f)
}
