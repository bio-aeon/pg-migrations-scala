package su.wps.pgmigrations

/**
  * This sealed trait's specifies the commit behavior on a database
  * connection and its subobjects are used as arguments to the
  * Migrator#with*Connection() methods.  The subobjects specify if a
  * new connection's auto-commit mode should be left on or disabled.
  * For connections with auto-commit mode disabled it specifies if the
  * current transaction should be rolled back or committed if the
  * closure passed to Migrator#with*Connection() throws an exception.
  */
private sealed trait CommitBehavior

/**
  * The new database connection's auto-commit mode is left on.  Because
  * the connection is in auto-commit mode the
  * Migrator#with*Connection() methods do not commit nor roll back the
  * transaction any before returning the result of the
  * Migrator#with*Connection()'s closure or rethrowing its exception.
  */
private case object AutoCommit extends CommitBehavior

/**
  * The new database connection's auto-commit mode is turned off.
  * Regardless if the closure passed to Migrator#with*Connection()
  * returns or throws an exception the transaction is committed.
  */
private case object CommitUponReturnOrException extends CommitBehavior

/**
  * The new database connection's auto-commit mode is turned off.  If
  * the closure passed to the Migrator#with*Connection() returns
  * normally then transaction is committed; if it throws an exception
  * then the transaction is rolled back.
  */
private case object CommitUponReturnOrRollbackUponException extends CommitBehavior
