package su.wps.pgmigrations

/**
  * Sealed trait abstracting the database to use for testing.
  */
sealed trait TestDatabase {
  /**
    * Get the schema name the tests are being run in.
    */
  def getSchemaName: String

  /**
    * Get the username of the admin account.
    */
  def getAdminAccountName: String

  /**
    * Get a connection builder that builds connections with access to
    * the entire schema.
    */
  def getAdminConnectionBuilder(url: String, username: String, password: String): ConnectionBuilder

  /**
    * Get the username of the user account.
    */
  def getUserAccountName: String

  /**
    * Get a connection builder that builds connections that connect as
    * a user with restricted privileges.
    */
  def getUserConnectionBuilder(url: String, username: String, password: String): ConnectionBuilder

  /**
    * The DatabaseAdapter to use for the test database.
    */
  def getDatabaseAdapter: DatabaseAdapter
}

/**
  * PostgreSQL test database implementation.
  */
object PostgresqlTestDatabase
  extends TestDatabase {
  // Username of the admin account, which will be the owner of the
  // database.
  private val adminUsername = {
    System.getProperty(TestDatabase.adminUserNameProperty, "test-admin")
  }

  override def getAdminAccountName = adminUsername

  // Username of the user account.
  private val userUsername = {
    System.getProperty(TestDatabase.userUserNameProperty, "test-user")
  }

  override def getUserAccountName = userUsername

  override def getSchemaName: String = {
    System.getProperty(TestDatabase.schemaNameProperty, "public")
  }

  // Load the PostgreSQL database driver.
  Class.forName("org.postgresql.Driver")

  override def getAdminConnectionBuilder(
      url: String,
      username: String,
      password: String): ConnectionBuilder =
    new ConnectionBuilder(url, username, password)

  override def getUserConnectionBuilder(
      url: String,
      username: String,
      password: String): ConnectionBuilder =
    new ConnectionBuilder(url, username, password)

  override def getDatabaseAdapter: DatabaseAdapter = {
    new PostgresqlDatabaseAdapter(Some(getSchemaName))
  }
}

/**
  * Object which builds the correct TestDatabase according to the
  * system property "scala-migrations.db.vendor", defaulting to Derby if
  * the property is not set.
  */
object TestDatabase
  extends TestDatabase {
  val adminUserNameProperty = "scala-migrations.db.admin.name"
  val adminUserPasswordProperty = "scala-migrations.db.admin.passwd"
  val databaseNameProperty = "scala-migrations.db.db"
  val schemaNameProperty = "scala-migrations.db.schema"
  val userUserNameProperty = "scala-migrations.db.user.name"
  val userUserPasswordProperty = "scala-migrations.db.user.passwd"
  val vendorNameProperty = "scala-migrations.db.vendor"

  private val db: TestDatabase = PostgresqlTestDatabase

  override def getSchemaName = db.getSchemaName

  override def getAdminAccountName = db.getAdminAccountName

  override def getAdminConnectionBuilder(url: String, username: String, password: String) =
    db.getAdminConnectionBuilder(url, username, password)

  override def getUserAccountName = db.getUserAccountName

  override def getUserConnectionBuilder(url: String, username: String, password: String) =
    db.getUserConnectionBuilder(url, username, password)

  override def getDatabaseAdapter = db.getDatabaseAdapter

  def execute(connectionBuilder: ConnectionBuilder,
              sql: String): Boolean = {
    connectionBuilder.withConnection(AutoCommit) { c =>
      With.autoClosingStatement(c.prepareStatement(sql)) { s =>
        s.execute()
      }
    }
  }
}
