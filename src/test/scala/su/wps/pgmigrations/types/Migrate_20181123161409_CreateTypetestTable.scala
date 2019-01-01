package su.wps.pgmigrations.types

import su.wps.pgmigrations.{Limit, Migration, Precision, Scale}

class Migrate_20181123161409_CreateTypetestTable extends Migration {
  val tableName = "scala_migrations_types_test"

  def up() {
    createTable(tableName) { t =>
      t.bigint("bigint_column")
      t.blob("blob_column")
      t.char("char_column", Limit(4))
      t.decimal("decimal_column", Precision(22), Scale(2))
      t.integer("integer_column")
      t.timestamp("timestamp_column")
      t.varbinary("varbinary_column", Limit(4))
      t.varchar("varchar_column", Limit(4))
      t.text("text_column")
      t.float("float_column")
      t.uuid("uuid_column")
    }
  }

  def down() {
    dropTable(tableName)
  }
}
