package su.wps.pgmigrations

import scala.collection.mutable

/**
  * A builder to define a table.  Its methods add the specified type of
  * column to the table's definition.
  */
class TableDefinition(adapter: DatabaseAdapter,
                      tableName: String) {
  private val columnDefinitions = new mutable.ListBuffer[ColumnDefinition]

  /**
    * Generate a SQL string representation of the columns in the table.
    *
    * @return the SQL text that defines the columns in the table
    */
  final def toSql: String = {
    val sb = new java.lang.StringBuilder(512)
    var firstColumn = true
    for (columnDefinition <- columnDefinitions) {
      if (firstColumn) {
        firstColumn = false
      }
      else {
        sb.append(", ")
      }
      sb.append(columnDefinition.getColumnName)
        .append(' ')
        .append(columnDefinition.toSql)
    }
    sb.toString
  }

  /**
    * Add any known column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param columnType the type of column being added
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def column(name: String,
                   columnType: SqlType,
                   options: ColumnOption*): TableDefinition = {
    val columnDefinition = adapter.newColumnDefinition(tableName,
      name,
      columnType,
      options: _*)
    columnDefinitions += columnDefinition
    this
  }

  /**
    * Add a BIGINT column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def bigint(name: String,
                   options: ColumnOption*): TableDefinition = {
    column(name, BigintType, options: _*)
  }

  /**
    * Add a BLOB column type to the table.  The actual SQL text used to
    * create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def blob(name: String,
                 options: ColumnOption*): TableDefinition = {
    column(name, BlobType, options: _*)
  }

  /**
    * Add a BOOLEAN column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def boolean(name: String,
                    options: ColumnOption*): TableDefinition = {
    column(name, BooleanType, options: _*)
  }

  /**
    * Add a CHAR column type to the table.  The actual SQL text used to
    * create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def char(name: String,
                 options: ColumnOption*): TableDefinition = {
    column(name, CharType, options: _*)
  }

  /**
    * Add a DECIMAL column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def decimal(name: String,
                    options: ColumnOption*): TableDefinition = {
    column(name, DecimalType, options: _*)
  }

  /**
    * Add a INTEGER column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def integer(name: String,
                    options: ColumnOption*): TableDefinition = {
    column(name, IntegerType, options: _*)
  }

  /**
    * Add a SMALLINT column type to the table.  The actual SQL text
    * used to create the column is chosen by the database adapter and
    * may be different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def smallint(name: String,
                     options: ColumnOption*): TableDefinition = {
    column(name, SmallintType, options: _*)
  }

  /**
    * Add a TIMESTAMP column type to the table.  The actual SQL text
    * used to create the column is chosen by the database adapter and
    * may be different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def timestamp(name: String,
                      options: ColumnOption*): TableDefinition = {
    column(name, TimestampType, options: _*)
  }

  /**
    * Add a VARBINARY column type to the table.  The actual SQL text
    * used to create the column is chosen by the database adapter and
    * may be different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def varbinary(name: String,
                      options: ColumnOption*): TableDefinition = {
    column(name, VarbinaryType, options: _*)
  }

  /**
    * Add a VARCHAR column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def varchar(name: String,
                    options: ColumnOption*): TableDefinition = {
    column(name, VarcharType, options: _*)
  }

  /**
    * Add a FLOAT column type to the table.  The actual SQL text used
    * to create the column is chosen by the database adapter and may be
    * different than the name of the columnType argument.
    *
    * @param name the column's name
    * @param options a possibly empty array of column options to customize the
    *        column
    * @return the same instance
    */
  final def float(name: String, options: ColumnOption*): TableDefinition = {
    column(name, FloatType, options: _*)
  }
}
