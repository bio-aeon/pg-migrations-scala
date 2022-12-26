package su.wps.pgmigrations

import java.sql.{Connection, DriverManager}
import javax.sql.DataSource

/**
  * Adapter class for getting a Connection from either the
  * DriverManager or a DataSource.
  */
class ConnectionBuilder private (
  either: Either[DataSource, String],
  loginOpt: Option[(String, String)]
) {

  /**
    * Construct a connection builder for a database that does not need
    * a username and password.
    *
    * @param url the JDBC URL to connect to the database
    */
  def this(url: String) {
    this(Right(url), None)
  }

  /**
    * Construct a connection builder for a database that needs a
    * username and password.
    *
    * @param url the JDBC URL to connect to the database
    * @param username the username to log into the database
    * @param password the password associated with the database
    *        username
    */
  def this(url: String, username: String, password: String) {
    this(Right(url), Some((username, password)))
  }

  /**
    * Construct a connection builder with a DataSource for a database
    * that does not need a username and password.
    *
    * @param datasource the JDBC DataSource to connect to the
    *        database
    */
  def this(datasource: DataSource) {
    this(Left(datasource), None)
  }

  /**
    * Construct a connection builder with a DataSource and override the
    * default username and password.
    *
    * @param datasource the JDBC DataSource to connect to the database
    * @param username the username to log into the database
    * @param password the password associated with the database
    *        username
    */
  def this(datasource: DataSource, username: String, password: String) {
    this(Left(datasource), Some((username, password)))
  }

  def withConnection[R](commitBehavior: CommitBehavior)(f: Function[Connection, R]): R = {
    val connection =
      (either, loginOpt) match {
        case (Left(datasource), Some((username, password))) =>
          datasource.getConnection(username, password)
        case (Left(datasource), None) =>
          datasource.getConnection
        case (Right(url), Some((username, password))) =>
          DriverManager.getConnection(url, username, password)
        case (Right(url), None) =>
          DriverManager.getConnection(url)
      }

    With.autoClosingConnection(connection) { c =>
      With.autoCommittingConnection(c, commitBehavior)(f)
    }
  }
}
