package su.wps.pgmigrations

/**
  * Sealed abstract base class for the case objects that represent the
  * supported SQL types.
  */
sealed abstract class SqlType

case object BigintType extends SqlType
case object BlobType extends SqlType
case object BooleanType extends SqlType
case object CharType extends SqlType
case object DecimalType extends SqlType
case object IntegerType extends SqlType
case object SmallintType extends SqlType
case object TimestampType extends SqlType
case object VarbinaryType extends SqlType
case object VarcharType extends SqlType
case object FloatType extends SqlType
case object UUIDType extends SqlType
