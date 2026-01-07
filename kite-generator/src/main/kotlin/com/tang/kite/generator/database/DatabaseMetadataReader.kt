package com.tang.kite.generator.database

import com.tang.kite.generator.config.GeneratorConfig
import com.tang.kite.generator.config.Language
import com.tang.kite.generator.info.ColumnInfo
import com.tang.kite.generator.info.TableInfo
import com.tang.kite.generator.info.TargetType
import com.tang.kite.generator.utils.serialversion.SerialVersionGenerator
import com.tang.kite.generator.utils.serialversion.FieldInfo
import com.tang.kite.logging.LOGGER
import com.tang.kite.utils.CaseFormat
import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.sql.Types
import javax.sql.DataSource

/**
 * Database metadata reader
 *
 * @author Tang
 */
class DatabaseMetadataReader(

    private val dataSource: DataSource,

    private val config: GeneratorConfig

) {

    /**
     * Get table information list
     */
    fun getTables(): List<TableInfo> {
        val connection = dataSource.connection
        val metaData = connection.metaData
        val tables = mutableListOf<TableInfo>()

        val types = getTableTypes(metaData)
        val resultSet = metaData.getTables(connection.catalog, null, null, types)
        while (resultSet.next()) {
            val tableInfo = getTableInfo(resultSet)
            tables.add(tableInfo)
        }
        resultSet.close()

        val tableNames = config.tableNames
        if (tableNames.isEmpty()) {
            return tables
        }
        val filteredTables = tables.filter { tableInfo ->
            tableNames.any { it.equals(tableInfo.tableName, ignoreCase = true) }
        }

        val unmatchedTables = tableNames.filter { tableName ->
            !tables.any { it.tableName.equals(tableName, ignoreCase = true) }
        }
        if (unmatchedTables.isNotEmpty()) {
            LOGGER.warn("The following table names were not found in the database: ${unmatchedTables.joinToString(", ")}")
        }

        return filteredTables.map { getTableInfo(metaData, it) }
    }

    private fun getTableTypes(metaData: DatabaseMetaData): Array<String> {
        val tableTypes = metaData.tableTypes
        val types = arrayListOf<String>()
        while (tableTypes.next()) {
            types.add(tableTypes.getString(1))
        }
        return types.toTypedArray()
    }

    private fun getTableInfo(resultSet: ResultSet): TableInfo {
        val tableName = resultSet.getString("TABLE_NAME")
        val tableComment = resultSet.getString("REMARKS") ?: ""
        return TableInfo(tableName, tableComment)
    }

    private fun getTableInfo(metaData: DatabaseMetaData, tableInfo: TableInfo): TableInfo {
        val tableName = tableInfo.tableName
        val className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.lowercase())
        val variableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.lowercase())
        val mappingName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, tableName.lowercase())

        val columns = getColumns(metaData, tableName)

        val fields = columns.map { FieldInfo(it.propertyName, it.targetType.getFullName()) }
        val serialVersionUID = SerialVersionGenerator.generate(className, fields)

        return TableInfo(tableName, tableInfo.comment, className, variableName, mappingName, serialVersionUID, columns)
    }

    /**
     * Get table column information
     */
    private fun getColumns(metaData: DatabaseMetaData, tableName: String): List<ColumnInfo> {
        val columns = mutableListOf<ColumnInfo>()

        val pkColumns = getPrimaryKeys(metaData, tableName)

        val colRs = metaData.getColumns(null, null, tableName, null)
        while (colRs.next()) {
            val columnName = colRs.getString("COLUMN_NAME")
            val dataType = colRs.getInt("DATA_TYPE")
            val typeName = colRs.getString("TYPE_NAME")
            val columnSize = colRs.getInt("COLUMN_SIZE")
            val decimalDigits = colRs.getInt("DECIMAL_DIGITS")
            val nullable = colRs.getInt("NULLABLE") == DatabaseMetaData.columnNullable
            val columnComment = colRs.getString("REMARKS") ?: ""
            val defaultValue = colRs.getString("COLUMN_DEF")
            val isAutoIncrement = colRs.getBoolean("IS_AUTOINCREMENT")

            val targetType = getTargetType(dataType, typeName, columnSize, decimalDigits)
            val propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName)
            val isPrimaryKey = pkColumns.contains(columnName.lowercase())

            columns.add(
                ColumnInfo(
                    columnName = columnName,
                    propertyName = propertyName,
                    dataType = typeName,
                    targetType = targetType,
                    comment = columnComment,
                    isNullable = nullable,
                    isPrimaryKey = isPrimaryKey,
                    isAutoIncrement = isAutoIncrement,
                    defaultValue = defaultValue
                )
            )
        }

        colRs.close()
        return columns
    }

    /**
     * Get primary key columns
     */
    private fun getPrimaryKeys(metaData: DatabaseMetaData, tableName: String): Set<String> {
        val pkColumns = mutableSetOf<String>()
        val pkRs = metaData.getPrimaryKeys(null, null, tableName)

        while (pkRs.next()) {
            val columnName = pkRs.getString("COLUMN_NAME")
            pkColumns.add(columnName.lowercase())
        }

        pkRs.close()
        return pkColumns
    }

    /**
     * Get Java/Kotlin type based on database type and target language
     */
    private fun getTargetType(dataType: Int, typeName: String, columnSize: Int, decimalDigits: Int): TargetType {
        val baseType = when (dataType) {
            Types.TINYINT, Types.SMALLINT, Types.INTEGER -> TargetType("Integer")
            Types.BIGINT -> TargetType("Long")
            Types.FLOAT, Types.REAL -> TargetType("Float")
            Types.DOUBLE -> TargetType("Double")
            Types.DECIMAL, Types.NUMERIC -> TargetType(getNumericBaseType(columnSize, decimalDigits))
            Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR -> TargetType("String")
            Types.NCHAR, Types.NVARCHAR, Types.LONGNVARCHAR -> TargetType("String")
            Types.DATE -> TargetType("java.time.LocalDate")
            Types.TIME -> TargetType("java.time.LocalTime")
            Types.TIMESTAMP -> TargetType("java.time.LocalDateTime")
            Types.BOOLEAN, Types.BIT -> TargetType("Boolean")
            Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> TargetType("byte[]")
            Types.CLOB -> TargetType("String")
            else -> TargetType(getBaseTypeFromDbTypeName(typeName))
        }
        return resolveTypeByLanguage(baseType, config.language)
    }

    /**
     * Get numeric base type based on precision and scale
     */
    private fun getNumericBaseType(columnSize: Int, decimalDigits: Int): String {
        return if (decimalDigits > 0) {
            "java.math.BigDecimal"
        } else {
            when {
                columnSize <= 9 -> "Integer"
                columnSize <= 18 -> "Long"
                else -> "java.math.BigDecimal"
            }
        }
    }

    /**
     * Get base type from database type name
     */
    private fun getBaseTypeFromDbTypeName(dbType: String): String {
        val normalizedType = dbType.lowercase()
        return baseTypeMappings[normalizedType] ?: "String"
    }

    /**
     * Resolve type name according to target language
     * Java: Wrapper classes (Integer/Long), byte[]
     * Kotlin: Primitive types (Int/Long), ByteArray
     */
    private fun resolveTypeByLanguage(baseType: TargetType, language: Language): TargetType {
        return if (language == Language.KOTLIN) {
            when (baseType.className) {
                "Integer" -> TargetType("Int")
                "Long" -> TargetType("Long")
                "Float" -> TargetType("Float")
                "Double" -> TargetType("Double")
                "Boolean" -> TargetType("Boolean")
                "byte[]" -> TargetType("ByteArray")
                else -> baseType
            }
        } else {
            baseType
        }
    }

    /**
     * Database Type → Java Base Type Mapping Table
     */
    private val baseTypeMappings = mapOf(
        // Numeric types
        "tinyint" to "Integer",
        "tinyint unsigned" to "Integer",
        "smallint" to "Integer",
        "smallint unsigned" to "Integer",
        "mediumint" to "Integer",
        "mediumint unsigned" to "Integer",
        "int" to "Integer",
        "integer" to "Integer",
        "int unsigned" to "Long",
        "integer unsigned" to "Long",
        "bigint" to "Long",
        "bigint unsigned" to "java.math.BigInteger",
        "float" to "Float",
        "double" to "Double",
        "decimal" to "java.math.BigDecimal",
        "numeric" to "java.math.BigDecimal",
        "real" to "Double",

        // Date/Time types
        "date" to "java.time.LocalDate",
        "datetime" to "java.time.LocalDateTime",
        "timestamp" to "java.time.LocalDateTime",
        "time" to "java.time.LocalTime",
        "year" to "Integer",
        "time with time zone" to "java.time.OffsetTime",
        "timestamp with time zone" to "java.time.OffsetDateTime",
        "timestamp with local time zone" to "java.time.LocalDateTime",

        // Binary types
        "binary" to "byte[]",
        "varbinary" to "byte[]",
        "tinyblob" to "byte[]",
        "blob" to "byte[]",
        "mediumblob" to "byte[]",
        "longblob" to "byte[]",
        "image" to "byte[]", // SQL Server
        "bytea" to "byte[]", // PostgreSQL

        // Boolean types
        "boolean" to "Boolean",
        "bit" to "Boolean",
        "bit(1)" to "Boolean",
        "bool" to "Boolean"
    )

}
