package su.wps.pgmigrations.scale_without_precision

import su.wps.pgmigrations.{Migration, Scale}

class Migrate_200812041647_Foo extends Migration {
  def up() {
    createTable("foo") { t =>
      t.decimal("bar", Scale(3))
    }
  }

  def down() {}
}
