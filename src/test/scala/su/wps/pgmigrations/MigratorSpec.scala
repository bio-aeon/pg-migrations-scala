package su.wps.pgmigrations

import com.dimafeng.testcontainers.PostgreSQLContainer
import org.postgresql.util.PGobject
import org.specs2.execute.Result
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach

class MigratorSpec extends Specification with ForAllTestContainer with BeforeEach {
  sequential

  val container = PostgreSQLContainer()

  lazy val connectionBuilder = TestDatabase.getAdminConnectionBuilder(
    container.jdbcUrl,
    container.username,
    container.password
  )

  lazy val databaseAdapter = TestDatabase.getDatabaseAdapter

  lazy val migrator = new Migrator(connectionBuilder, databaseAdapter)

  def before: Unit =
    connectionBuilder.withConnection(AutoCommit) { c =>
      for (tableName <- migrator.getTableNames) {
        val tn = tableName.toLowerCase
        if (tn == "schema_migrations" || tn.startsWith("scala_migrations_")) {
          val sql = "DROP TABLE " + databaseAdapter.quoteTableName(tn)
          With.autoClosingStatement(c.prepareStatement(sql)) { _.execute() }
        }
      }
    }

  "Migrator should" >> {
    "throw an error for duplicated migrations descriptions" >> {
      migrator.migrate(
        InstallAllMigrations,
        Seq("su.wps.pgmigrations.duplicate_descriptions"),
        searchSubPackages = false
      ) must throwA[DuplicateMigrationDescriptionException]
    }

    "throw an error for duplicated migrations versions" >> {
      migrator.migrate(
        InstallAllMigrations,
        Seq("su.wps.pgmigrations.duplicate_versions"),
        searchSubPackages = false
      ) must throwA[DuplicateMigrationVersionException]
    }

    "throw an error for migrations with invalid decimal value" >> {
      migrator.migrate(
        InstallAllMigrations,
        Seq("su.wps.pgmigrations.scale_without_precision"),
        searchSubPackages = false
      ) must throwA[IllegalArgumentException]
    }

    "up and down migrations" >> {
      // There should be no tables in the schema initially.
      migrator.getTableNames must haveSize(0)

      // Migrate down the whole way.
      migrator.migrate(
        RemoveAllMigrations,
        Seq("su.wps.pgmigrations.up_and_down"),
        searchSubPackages = false
      )

      // There should only be the schema_migrations table now.
      migrator.getTableNames must haveSize(1)
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_location") must beFalse
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_people") must beFalse

      // The database should not be completely migrated.
      migrator.whyNotMigrated(Seq("su.wps.pgmigrations.up_and_down"), searchSubPackages = false) must beSome

      val statuses1 =
        migrator.getMigrationStatuses(
          Seq("su.wps.pgmigrations.up_and_down"),
          searchSubPackages = false
        )

      statuses1.notInstalled must haveSize(2)
      statuses1.notInstalled must haveKey(20081118201000L)
      statuses1.notInstalled must haveKey(20081118201742L)
      statuses1.installedWithAvailableImplementation must haveSize(0)
      statuses1.installedWithoutAvailableImplementation must haveSize(0)

      // Apply all the migrations.
      migrator.migrate(
        InstallAllMigrations,
        Seq("su.wps.pgmigrations.up_and_down"),
        searchSubPackages = false
      )

      migrator.getTableNames must haveSize(3)
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_location") must beTrue
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_people") must beTrue

      // The database should be completely migrated.
      migrator.whyNotMigrated(Seq("su.wps.pgmigrations.up_and_down"), searchSubPackages = false) must beNone

      // With a empty set of migrations the database should not be
      // completely migrated.
      migrator.whyNotMigrated(Seq("su.wps.pgmigrations.no_migrations"), searchSubPackages = true) must beSome

      val statuses2 =
        migrator.getMigrationStatuses(
          Seq("su.wps.pgmigrations.up_and_down"),
          searchSubPackages = false
        )

      statuses2.notInstalled must haveSize(0)
      statuses2.installedWithAvailableImplementation must haveSize(2)
      statuses2.installedWithAvailableImplementation must haveKey(20081118201000L)
      statuses2.installedWithAvailableImplementation must haveKey(20081118201742L)
      statuses2.installedWithoutAvailableImplementation must haveSize(0)

      // Rollback a single migration.
      migrator.migrate(
        RollbackMigration(1),
        Seq("su.wps.pgmigrations.up_and_down"),
        searchSubPackages = false
      )

      // There should only be the schema_migrations and
      // scala_migrations_location tables now.
      migrator.getTableNames must haveSize(2)
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_location") must beTrue
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_people") must beFalse

      // The database should not be completely migrated.
      migrator.whyNotMigrated(Seq("su.wps.pgmigrations.up_and_down"), searchSubPackages = false) must beSome

      // With a empty set of migrations the database should not be
      // completely migrated.
      migrator.whyNotMigrated(Seq("su.wps.pgmigrations.no_migrations"), searchSubPackages = true) must beSome

      val statuses3 =
        migrator.getMigrationStatuses(
          Seq("su.wps.pgmigrations.up_and_down"),
          searchSubPackages = false
        )

      statuses3.notInstalled must haveSize(1)
      statuses3.notInstalled must haveKey(20081118201742L)
      statuses3.installedWithAvailableImplementation must haveSize(1)
      statuses3.installedWithAvailableImplementation must haveKey(20081118201000L)
      statuses3.installedWithoutAvailableImplementation must haveSize(0)

      // Migrate down the whole way.
      migrator.migrate(
        RemoveAllMigrations,
        Seq("su.wps.pgmigrations.up_and_down"),
        searchSubPackages = false
      )

      // There should only be the schema_migrations table now.
      migrator.getTableNames must haveSize(1)
      migrator.getTableNames.exists(_.toLowerCase == "scala_migrations_people") must beFalse

      // The database should not be completely migrated.
      migrator.whyNotMigrated(Seq("su.wps.pgmigrations.up_and_down"), searchSubPackages = false) must beSome

      val statuses4 =
        migrator.getMigrationStatuses(
          Seq("su.wps.pgmigrations.up_and_down"),
          searchSubPackages = false
        )

      statuses4.notInstalled.size mustEqual 2
      statuses4.notInstalled must haveKey(20081118201000L)
      statuses4.notInstalled must haveKey(20081118201742L)
      statuses4.installedWithAvailableImplementation must haveSize(0)
      statuses4.installedWithoutAvailableImplementation must haveSize(0)
    }

    "work with different types" >> {
      migrator.migrate(
        InstallAllMigrations,
        Seq("su.wps.pgmigrations.types"),
        searchSubPackages = false
      )

      val varbinaryArray = (1 to 4).map(_.toByte).toArray
      val now = System.currentTimeMillis

      val counts = migrator.withLoggingConnection(AutoCommit) { c =>
        val o = new PGobject
        o.setType("jsonb")
        o.setValue("{}")

        List(
          ("bigint_column", java.lang.Long.MIN_VALUE),
          ("bigint_column", java.lang.Long.MAX_VALUE),
          ("char_column", "ABCD"),
          ("decimal_column", 3.14),
          ("integer_column", java.lang.Integer.MIN_VALUE),
          ("integer_column", java.lang.Integer.MAX_VALUE),
          ("timestamp_column", new java.sql.Date(now)),
          ("timestampz_column", new java.sql.Date(now)),
          ("varbinary_column", varbinaryArray),
          ("varchar_column", "ABCD"),
          ("text_column", "ABCD"),
          ("float_column", 3.14f),
          ("uuid_column", java.util.UUID.randomUUID()),
          ("jsonb_column", o)
        ).map {
          case (n, v) =>
            val insertSql = """INSERT INTO
                             scala_migrations_types_test (""" + n + """)
                           VALUES
                             (?)""".replaceAll("\\s+", " ")
            val insertStatement = c.prepareStatement(insertSql)
            insertStatement.setObject(1, v)
            insertStatement.executeUpdate()
            insertStatement.close()

            // Make sure that the value exists.
            val selectSql = """SELECT
                             COUNT(1)
                           FROM
                             scala_migrations_types_test
                           WHERE
                             """ + n + """ = ?""".replaceAll("\\s+", " ")
            With.autoClosingStatement(c.prepareStatement(selectSql)) { s =>
              s.setObject(1, v)
              With.autoClosingResultSet(s.executeQuery()) { rs =>
                var counts: List[Int] = Nil
                while (rs.next()) {
                  counts = rs.getInt(1) :: counts
                }

                counts
              }
            }
        }
      }

      Result.foreach(counts) { counts =>
        counts.size mustEqual 1
        counts.head mustEqual 1
      }
    }
  }
}
