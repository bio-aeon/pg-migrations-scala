package su.wps.pgmigrations

class TableColumnDefinition(val tableName: String, val columnNames: Array[String])

/**
  * A container class for storing the table and column names a foreign
  * key reference is on.
  */
class On(definition: TableColumnDefinition) {
  val tableName = definition.tableName
  val columnNames = definition.columnNames
}

/**
  * A container class for storing the table and column names a foreign
  * key reference references.
  */
class References(definition: TableColumnDefinition) {
  val tableName = definition.tableName
  val columnNames = definition.columnNames
}
