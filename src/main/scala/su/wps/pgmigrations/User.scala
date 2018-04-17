package su.wps.pgmigrations

/**
  * Representation of a SQL user.  It provides a single #quoted()
  * method that returns the user properly quoted for inclusion in a SQL
  * statement.
  */
sealed abstract class User {
  /**
    * The user quoted for a SQL statement.
    *
    * @param unquotedNameConverter the unquoted name converter for the
    *        database
    * @return the user quoted for a SQL statement
    */
  def quoted(unquotedNameConverter: UnquotedNameConverter): String
}

/**
  * A user consisting only of a user name.  Uses double quotation marks
  * to quote the user name.
  *
  * @param userName the user name
  */
class PlainUser(userName: String)
  extends User {
  override def quoted(unquotedNameConverter: UnquotedNameConverter): String = {
    '"' + unquotedNameConverter(userName) + '"'
  }
}

/**
  * Object to create PlainUser instances with a user name with a nice
  * syntax, e.g. User('foobar').
  */
object User {
  /**
    * Given a user name, return a PlainUser instance.
    *
    * @param userName a user name
    * @return a PlainUser with the given name
    */
  def apply(userName: String): PlainUser = new PlainUser(userName)
}

object MysqlUser {
  /**
    * Given a user name and a host name return a User appropriate for a
    * MySQL database, see
    * http://dev.mysql.com/doc/refman/5.5/en/account-names.html .
    *
    * @param userName a user name
    * @param hostName a host name
    */
  def apply(userName: String,
            hostName: String): MysqlUser = {
    new MysqlUser(userName, hostName)
  }
}

/**
  * Representation of a SQL user for MySQL which consists of a user
  * name and a host name; see
  * http://dev.mysql.com/doc/refman/5.5/en/account-names.html .
  *
  * @param userName the user name
  * @param hostName the host name
  */
class MysqlUser(userName: String,
                hostName: String)
  extends User {
  override def quoted(unquotedNameConverter: UnquotedNameConverter): String = {
    val sb = new java.lang.StringBuilder(64)
    sb.append('\'')
      .append(unquotedNameConverter(userName))
      .append("'@'")
      .append(unquotedNameConverter(hostName))
      .append('\'')
      .toString
  }
}

/**
  * A factory for User instances that are built from a user name.
  */
abstract class UserFactory[T <: User] {
  /**
    * Given a user name, return a User instance.
    *
    * @param userName a user name
    * @return a User with the given name
    */
  def nameToUser(userName: String): T
}

/**
  * Singleton UserFactory that creates PlainUser instances.
  */
object PlainUserFactory
  extends UserFactory[PlainUser] {
  override def nameToUser(userName: String) = User(userName)
}

/**
  * A singleton user factory to create MysqlUser instances.  This
  * factory uses the input user name and defaults the host name to
  * "localhost".
  */
object MysqlUserFactory
  extends UserFactory[MysqlUser] {
  override def nameToUser(userName: String): MysqlUser = {
    new MysqlUser(userName, "localhost")
  }
}
