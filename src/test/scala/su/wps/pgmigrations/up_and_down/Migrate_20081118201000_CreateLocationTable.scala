package su.wps.pgmigrations.up_and_down

import su.wps.pgmigrations.{Limit, Migration, NotNull, PrimaryKey, Unique}

class Migrate_20081118201000_CreateLocationTable extends Migration {
  val tableName = "scala_migrations_location"

  def up() {
    createTable(tableName) { t =>
      t.varbinary("pk_" + tableName, PrimaryKey, Limit(16))
      t.varchar("name", Unique, Limit(255), NotNull)
    }
  }

  def down() {
    dropTable(tableName)
  }
}
