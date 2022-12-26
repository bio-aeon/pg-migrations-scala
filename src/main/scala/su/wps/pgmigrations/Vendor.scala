package su.wps.pgmigrations

/**
  * Base sealed trait for the objects that refer to different
  * databases.
  */
sealed trait Vendor

case object Postgresql extends Vendor

object Vendor {

  /**
    * Return the database vendor for the given database driver class
    * name.
    *
    * @param driverClassName the class name of the JDBC database driver
    * @return the corresponding Vendor object for the database
    * @throws IllegalArgumentException if the argument is null,
    *         scala.MatchError if an appropriate vendor cannot be found
    */
  def forDriver(driverClassName: String): Vendor =
    driverClassName match {
      case "org.postgresql.Driver" =>
        Postgresql

      case null =>
        throw new IllegalArgumentException(
          "Must pass a non-null JDBC " +
            "driver class name to this " +
            "function."
        )

      case _ =>
        throw new scala.MatchError(
          "No vendor can be found for the JDBC " +
            "driver class '" +
            driverClassName +
            "'.'"
        )
    }

  /**
    * Return the database vendor for the given database driver class.
    *
    * @param driverClass the class of the JDBC database driver
    * @return the corresponding Vendor object for the database
    * @throws IllegalArgumentException if the argument is null,
    *         scala.MatchError if an appropriate vendor cannot be found
    */
  def forDriver(driverClass: Class[_]): Vendor =
    if (driverClass eq null) {
      val message = "Must pass a non-null JDBC driver class to this function."
      throw new IllegalArgumentException(message)
    } else {
      forDriver(driverClass.getName)
    }
}
