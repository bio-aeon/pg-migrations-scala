package su.wps.pgmigrations

class PostgresqlBigintColumnDefinition
    extends DefaultBigintColumnDefinition
    with ColumnSupportsAutoIncrement {
  override protected def sql: String =
    if (isAutoIncrement) "BIGSERIAL"
    else super.sql
}

class PostgresqlByteaColumnDefinition
    extends DefaultBlobColumnDefinition
    with ColumnSupportsDefault {
  override protected def sql = "BYTEA"
}

class PostgresqlIntegerColumnDefinition
    extends DefaultIntegerColumnDefinition
    with ColumnSupportsAutoIncrement {
  override protected def sql: String =
    if (isAutoIncrement) "SERIAL"
    else super.sql
}

class PostgresqlSmallintColumnDefinition
    extends DefaultSmallintColumnDefinition
    with ColumnSupportsAutoIncrement {
  override protected def sql: String =
    if (isAutoIncrement) "SMALLSERIAL"
    else super.sql
}

class PostgresqlDatabaseAdapter(override val schemaNameOpt: Option[String])
    extends DatabaseAdapter(schemaNameOpt) {
  override val vendor = Postgresql

  override val quoteCharacter = '"'

  override val unquotedNameConverter = LowercaseUnquotedNameConverter

  override val userFactory = PlainUserFactory

  override val alterTableDropForeignKeyConstraintPhrase = "CONSTRAINT"

  override val addingForeignKeyConstraintCreatesIndex = false

  override val supportsCheckConstraints = true

  override def columnDefinitionFactory(
    columnType: SqlType,
    characterSetOpt: Option[CharacterSet]
  ): ColumnDefinition = {
    characterSetOpt match {
      case None =>
      case Some(charset @ CharacterSet(_, _)) =>
        logger.warn(
          "Ignoring '{}' as the character set encoding can only " +
            "be specified in PostgreSQL when the database is created.",
          charset
        )
    }

    columnType match {
      case BigintType =>
        new PostgresqlBigintColumnDefinition
      case BlobType =>
        new PostgresqlByteaColumnDefinition
      case BooleanType =>
        new DefaultBooleanColumnDefinition
      case CharType =>
        new DefaultCharColumnDefinition
      case DecimalType =>
        new DefaultDecimalColumnDefinition
      case IntegerType =>
        new PostgresqlIntegerColumnDefinition
      case SmallintType =>
        new PostgresqlSmallintColumnDefinition
      case TimestampType =>
        new DefaultTimestampColumnDefinition
      case TimestampzType =>
        new DefaultTimestampzColumnDefinition
      case VarbinaryType =>
        new PostgresqlByteaColumnDefinition
      case VarcharType =>
        new DefaultVarcharColumnDefinition
      case TextType =>
        new DefaultTextColumnDefinition
      case FloatType =>
        new DefaultFloatColumnDefinition
      case UUIDType =>
        new DefaultUUIDColumnDefinition
      case JSONBType =>
        new DefaultJSONBColumnDefinition
    }
  }

  override protected def alterColumnSql(
    schemaNameOpt: Option[String],
    columnDefinition: ColumnDefinition
  ): String =
    new java.lang.StringBuilder(512)
      .append("ALTER TABLE ")
      .append(quoteTableName(schemaNameOpt, columnDefinition.getTableName))
      .append(" ALTER COLUMN ")
      .append(quoteColumnName(columnDefinition.getColumnName))
      .append(" TYPE ")
      .append(columnDefinition.toSql)
      .toString
}
