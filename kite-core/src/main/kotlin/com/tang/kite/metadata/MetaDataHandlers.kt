package com.tang.kite.metadata

import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.JDBCType
import kotlin.use

/**
 * @author Tang
 */
object MetaDataHandlers {

    private val tableTypes = arrayOf("TABLE")

    @JvmStatic
    fun getTable(connection: Connection, tableName: String): TableMeta? {
        return getTables(connection, tableName).firstOrNull()
    }

    @JvmOverloads
    @JvmStatic
    fun getTables(connection: Connection, tableNamePattern: String? = null): List<TableMeta> {
        val metaData = connection.metaData
        val resultSet = metaData.getTables(connection.catalog, connection.schema, tableNamePattern?.uppercase(), tableTypes)
        val tables = mutableListOf<TableMeta>()
        while (resultSet.next()) {
            tables.add(
                TableMeta(
                    catalog = resultSet.getString("TABLE_CAT"),
                    schema = resultSet.getString("TABLE_SCHEM"),
                    tableName = resultSet.getString("TABLE_NAME"),
                    tableType = resultSet.getString("TABLE_TYPE"),
                    comment = resultSet.getString("REMARKS")
                )
            )
        }
        resultSet.close()
        return tables
    }

    @JvmStatic
    fun getColumns(connection: Connection, tableName: String): List<ColumnMeta> {
        val metaData = connection.metaData
        val primaryKeySet = getPrimaryKeys(connection, tableName)
        val uniqueKeySet = getUniqueKeys(connection, tableName)

        val columnList = mutableListOf<ColumnMeta>()
        metaData.getColumns(connection.catalog, connection.schema, tableName.uppercase(), null).use {
            while (it.next()) {
                columnList.add(
                    ColumnMeta(
                        catalog = it.getString("TABLE_CAT"),
                        schema = it.getString("TABLE_SCHEM"),
                        tableName = it.getString("TABLE_NAME"),
                        columnName = it.getString("COLUMN_NAME"),
                        dataType = it.getInt("DATA_TYPE"),
                        typeName = it.getString("TYPE_NAME"),
                        jdbcType = JDBCType.valueOf(it.getInt("DATA_TYPE")),
                        columnSize = it.getInt("COLUMN_SIZE"),
                        decimalDigits = it.getObject("DECIMAL_DIGITS") as? Int ?: 0,
                        nullable = it.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
                        defaultValue = it.getString("COLUMN_DEF"),
                        ordinalPosition = it.getInt("ORDINAL_POSITION"),
                        comment = it.getString("REMARKS"),
                        primaryKey = primaryKeySet.contains(it.getString("COLUMN_NAME")),
                        unique = uniqueKeySet.contains(it.getString("COLUMN_NAME")),
                        autoIncrement = "YES".equals(it.getString("IS_AUTOINCREMENT"), ignoreCase = true),
                        generatedColumn = "YES".equals(it.getString("IS_GENERATEDCOLUMN"), ignoreCase = true)
                    )
                )
            }
        }
        return columnList
    }

    /**
     * Get primary key columns
     *
     * @param connection Database connection
     * @param tableName Table name
     * @return Primary key columns
     */
    @JvmStatic
    fun getPrimaryKeys(connection: Connection, tableName: String): Set<String> {
        val metaData = connection.metaData
        val primaryKeyColumns = mutableSetOf<String>()
        val resultSet = metaData.getPrimaryKeys(connection.catalog, connection.schema, tableName.uppercase())
        while (resultSet.next()) {
            val columnName = resultSet.getString("COLUMN_NAME")
            primaryKeyColumns.add(columnName.lowercase())
        }
        resultSet.close()
        return primaryKeyColumns
    }

    /**
     * Get all unique key columns (primary key + unique constraints)
     * @param connection database connection
     * @param tableName target table name
     * @return set of unique column names
     */
    @JvmStatic
    fun getUniqueKeys(connection: Connection, tableName: String): Set<String> {
        val uniqueColumns = getPrimaryKeys(connection, tableName).toMutableSet()
        val metaData = connection.metaData

        metaData.getIndexInfo(connection.catalog, connection.schema, tableName.uppercase(), true, true).use { rs ->
            while (rs.next()) {
                val indexName = rs.getString("INDEX_NAME")
                val column = rs.getString("COLUMN_NAME")?.lowercase()
                val type = rs.getShort("TYPE")

                if (indexName == null || column == null || type == 0.toShort()) continue
                uniqueColumns.add(column)
            }
        }
        return uniqueColumns
    }

    /**
     * Get unique indexes (excluding primary key)
     * @param connection database connection
     * @param tableName target table name
     * @return map: index name -> set of columns
     */
    @JvmStatic
    fun getUniqueIndexes(connection: Connection, tableName: String): Map<String, Set<String>> {
        val primaryKeys = getPrimaryKeys(connection, tableName)
        val result = mutableMapOf<String, MutableSet<String>>()
        val metaData = connection.metaData

        metaData.getIndexInfo(connection.catalog, connection.schema, tableName.uppercase(), true, true).use { rs ->
            while (rs.next()) {
                val indexName = rs.getString("INDEX_NAME")
                val column = rs.getString("COLUMN_NAME")?.lowercase()
                val nonUnique = rs.getBoolean("NON_UNIQUE")
                val type = rs.getShort("TYPE")

                if (indexName == null || column == null || type == 0.toShort() || nonUnique) continue
                if (column in primaryKeys) continue

                result.computeIfAbsent(indexName) { mutableSetOf() }.add(column)
            }
        }
        return result
    }

    /**
     * Get all indexes (primary, unique, regular) with full metadata
     * @param connection database connection
     * @param tableName target table name
     * @return map: index name -> IndexMeta
     */
    @JvmStatic
    fun getIndexes(connection: Connection, tableName: String): Map<String, IndexMeta> {
        val primaryKeys = getPrimaryKeys(connection, tableName)
        val metaData = connection.metaData
        val indexMap = mutableMapOf<String, IndexMeta>()

        metaData.getIndexInfo(connection.catalog, connection.schema, tableName.uppercase(), false, true).use { rs ->
            while (rs.next()) {
                val indexName = rs.getString("INDEX_NAME")
                val columnName = rs.getString("COLUMN_NAME")?.lowercase()
                val typeCode = rs.getShort("TYPE")

                // Skip statistic rows and invalid entries
                if (indexName == null || columnName == null || typeCode == 0.toShort()) continue

                val nonUnique = rs.getBoolean("NON_UNIQUE")
                val unique = !nonUnique
                val isPrimaryKey = columnName in primaryKeys

                val indexType = when (typeCode) {
                    1.toShort() -> IndexType.CLUSTERED
                    2.toShort() -> IndexType.HASHED
                    else -> IndexType.OTHER
                }

                val indexMeta = indexMap.computeIfAbsent(indexName) {
                    IndexMeta(
                        catalog = rs.getString("TABLE_CAT"),
                        schema = rs.getString("TABLE_SCHEM"),
                        tableName = rs.getString("TABLE_NAME"),
                        indexName = indexName,
                        indexType = indexType,
                        unique = unique,
                        cardinality = rs.getLong("CARDINALITY"),
                        pages = rs.getLong("PAGES"),
                        filterCondition = rs.getString("FILTER_CONDITION"),
                        isPrimaryKey = isPrimaryKey
                    )
                }

                // Maintain column order and sort direction
                if (columnName !in indexMeta.columns) {
                    indexMeta.columns.add(columnName)
                    indexMeta.columnSortMap[columnName] = rs.getString("ASC_OR_DESC")
                }
            }
        }

        return indexMap
    }

    /**
     * Get table types
     *
     * @param metaData Database metadata
     * @return Table types
     */
    @JvmStatic
    fun getTableTypes(metaData: DatabaseMetaData): Array<String> {
        val tableTypes = metaData.tableTypes
        val types = arrayListOf<String>()
        while (tableTypes.next()) {
            types.add(tableTypes.getString(1))
        }
        return types.toTypedArray()
    }

    /**
     * Check if table exists
     *
     * @param connection Database connection
     * @param tableNamePattern Table name pattern
     * @return True if table exists, false otherwise
     */
    @JvmStatic
    fun tableExists(connection: Connection, tableNamePattern: String): Boolean {
        if (tableNamePattern.isBlank()) return false
        return runCatching {
            connection.use { connection ->
                val metaData = connection.metaData
                val resultSet = metaData.getTables(connection.catalog, connection.schema, tableNamePattern, tableTypes)
                resultSet.use { it.next() }
            }
        }.getOrDefault(false)
    }

}
