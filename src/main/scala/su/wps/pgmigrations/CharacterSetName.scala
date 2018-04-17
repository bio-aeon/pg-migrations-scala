package su.wps.pgmigrations

/**
  * The base trait for all character set names.
  */
sealed trait CharacterSetName

/**
  * The character data types should be encoded using Unicode.
  *
  * On Derby, character data types are encoded using Unicode by
  * default so specifying this does not change the encoding.
  *
  * On MySQL, this case object specifies the "utf8" character set.  If
  * the collation is not specified then the "utf8_unicode_ci" collation
  * is used.  See http://stackoverflow.com/questions/766809/ why this
  * slower collation is used instead the "utf8_general_ci" collation,
  * which is MySQL's default collation for the "utf8" character set.
  *
  * On Oracle, this specifies that NCHAR is used for CharType and
  * NVARCHAR2 for VarcharType.
  *
  * On PostgreSQL, this case object is ignored as the character set
  * encoding can only be specified when the database is created; using
  * it generates a runtime warning.
  */
case object Unicode
  extends CharacterSetName
