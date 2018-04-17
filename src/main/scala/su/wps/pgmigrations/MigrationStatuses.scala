package su.wps.pgmigrations

/**
  * Container for the state of all the available and installed
  * migrations.
  *
  * @param notInstalled a sorted map of migration version numbers to
  *        Migration subclasses that are not installed in the database
  * @param installedWithAvailableImplementation a sorted map of
  *        migration version numbers to Migration subclasses that are
  *        currently installed in the database that have a matching a
  *        Migration subclass
  * @param installedWithoutAvailableImplementation a sorted set of
  *        migration version numbers that are currently installed in
  *        the database but do not have a matching a Migration subclass
  */
case class MigrationStatuses(notInstalled: scala.collection.SortedMap[Long, Class[_ <: Migration]],
                             installedWithAvailableImplementation: scala.collection.SortedMap[Long, Class[_ <: Migration]],
                             installedWithoutAvailableImplementation: scala.collection.SortedSet[Long])
