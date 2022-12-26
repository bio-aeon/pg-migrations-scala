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
class PlainUser(userName: String) extends User {
  override def quoted(unquotedNameConverter: UnquotedNameConverter): String =
    '"' + unquotedNameConverter(userName) + '"'
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
object PlainUserFactory extends UserFactory[PlainUser] {
  override def nameToUser(userName: String) = User(userName)
}
