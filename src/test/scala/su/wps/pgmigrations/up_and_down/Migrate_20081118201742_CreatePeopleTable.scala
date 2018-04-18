package su.wps.pgmigrations.up_and_down

import su.wps.pgmigrations._

class Migrate_20081118201742_CreatePeopleTable
    extends Migration {
  val tableName = "scala_migrations_people"

  def up() {
    createTable(tableName) { t =>
      t.varbinary("pk_" + tableName, PrimaryKey, Limit(16))
      t.varbinary("pk_scala_migrations_location", Limit(16), NotNull)
      t.integer("employee_id", Unique)
      t.integer("ssn", NotNull)
      t.varchar("first_name", Limit(255), NotNull,
        CharacterSet(Unicode, "utf8_unicode_ci"))
      t.char("middle_initial", Limit(1), Nullable)
      t.varchar("last_name", Limit(255), NotNull, CharacterSet(Unicode))
      t.timestamp("birthdate", Limit(0), NotNull)
      t.smallint("height", NotNull, NamedCheck("chk_height_nonnegative",
        "height > 0"))
      t.smallint("weight", NotNull, Check("weight > 0"))
      t.integer("vacation_days", NotNull, Default("0"))
      t.bigint("hire_time_micros", NotNull)
      t.decimal("salary", Precision(7), Scale(2), Check("salary > 0"))
      t.blob("image")
    }

    addIndex(tableName, "ssn", Unique)

    addForeignKey(on(tableName ->
      "pk_scala_migrations_location"),
      references("scala_migrations_location" ->
        "pk_scala_migrations_location"),
      OnDelete(Cascade),
      OnUpdate(Restrict),
      Name("fk_smp_pk_sml_sml_pk_sml"))

    if (!addingForeignKeyConstraintCreatesIndex) {
      addIndex(tableName,
        "pk_scala_migrations_location",
        Name("idx_smp_pk_sml"))
    }

    addColumn(tableName, "secret_key", VarbinaryType, Limit(16))

    addCheck(on(tableName -> "vacation_days"), "vacation_days >= 0")
  }

  def down() {
    removeCheck(on(tableName -> "vacation_days"))
    removeForeignKey(on(tableName ->
      "pk_scala_migrations_location"),
      references("scala_migrations_location" ->
        "pk_scala_migrations_location"),
      Name("fk_smp_pk_sml_sml_pk_sml"))
    if (!addingForeignKeyConstraintCreatesIndex) {
      removeIndex(tableName,
        "pk_scala_migrations_location",
        Name("idx_smp_pk_sml"))
    }

    removeIndex(tableName, "ssn")
    removeColumn(tableName, "secret_key")
    dropTable(tableName)
  }
}
