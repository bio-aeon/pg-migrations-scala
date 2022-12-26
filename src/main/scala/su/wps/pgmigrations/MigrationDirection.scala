package su.wps.pgmigrations

/**
  * Sealed abstract class that defines the direction to run a
  * migration.
  */
sealed abstract class MigrationDirection {

  /**
    * A human readable string representing the migration direction.
    */
  val str: String
}

/**
  * Case object used to indicate that a migration should be installed.
  */
case object Up extends MigrationDirection {
  override val str = "up"
}

/**
  * Case object used to indicate that a migration should be removed.
  */
case object Down extends MigrationDirection {
  override val str = "down"
}
