package com.tang.kite.metadata

import com.tang.kite.datasource.DatabaseValue
import java.sql.DatabaseMetaData
import java.sql.JDBCType
import kotlin.use

/**
 * @author Tang
 */
object MetaDataHandlers {

    private val tableTypes = arrayOf("TABLE")

    @JvmStatic
    fun getTable(databaseValue: DatabaseValue, tableName: String): TableMeta? {
        return getTables(databaseValue, tableName).firstOrNull()
    }

    @JvmOverloads
    @JvmStatic
    fun getTables(databaseValue: DatabaseValue, tableNamePattern: String? = null): List<TableMeta> {
        databaseValue.dataSource.connection.use { connection ->
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
    }

    @JvmStatic
    fun getColumns(databaseValue: DatabaseValue, tableName: String): List<ColumnMeta> {
        databaseValue.dataSource.connection.use { connection ->
            val metaData = connection.metaData
            val primaryKeySet = getPrimaryKeys(databaseValue, tableName)
            val uniqueKeySet = getUniqueKeys(databaseValue, tableName)

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
    }

    /**
     * Get primary key columns
     *
     * @param databaseValue Database value
     * @param tableName Table name
     * @return Primary key columns
     */
    @JvmStatic
    fun getPrimaryKeys(databaseValue: DatabaseValue, tableName: String): Set<String> {
        databaseValue.dataSource.connection.use { connection ->
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
    }

    /**
     * Get all unique key columns (primary key + unique constraints)
     * @param databaseValue Database value
     * @param tableName Table name
     * @return set of unique column names
     */
    @JvmStatic
    fun getUniqueKeys(databaseValue: DatabaseValue, tableName: String): Set<String> {
        val uniqueColumns = getPrimaryKeys(databaseValue, tableName).toMutableSet()
        databaseValue.dataSource.connection.use { connection ->
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
    }

    /**
     * Get unique indexes (excluding primary key)
     * @param databaseValue Database value
     * @param tableName target table name
     * @return map: index name -> set of columns
     */
    @JvmStatic
    fun getUniqueIndexes(databaseValue: DatabaseValue, tableName: String): Map<String, Set<String>> {
        val primaryKeys = getPrimaryKeys(databaseValue, tableName)
        databaseValue.dataSource.connection.use { connection ->
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
    }

    /**
     * Get all indexes (primary, unique, regular) with full metadata
     * @param databaseValue Database value
     * @param tableName target table name
     * @return map: index name -> IndexMeta
     */
    @JvmStatic
    fun getIndexes(databaseValue: DatabaseValue, tableName: String): Map<String, IndexMeta> {
        val primaryKeys = getPrimaryKeys(databaseValue, tableName)
        databaseValue.dataSource.connection.use { connection ->
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

                    val indexStructure = when (typeCode) {
                        1.toShort() -> IndexStructure.CLUSTERED
                        2.toShort() -> IndexStructure.HASH
                        3.toShort() -> IndexStructure.BTREE
                        else -> IndexStructure.OTHER
                    }

                    val indexMeta = indexMap.computeIfAbsent(indexName) {
                        IndexMeta(
                            catalog = rs.getString("TABLE_CAT"),
                            schema = rs.getString("TABLE_SCHEM"),
                            tableName = rs.getString("TABLE_NAME"),
                            indexName = indexName,
                            indexStructure = indexStructure,
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
     * @param databaseValue Database value
     * @param tableNamePattern Table name pattern
     * @return True if table exists, false otherwise
     */
    @JvmStatic
    fun tableExists(databaseValue: DatabaseValue, tableNamePattern: String): Boolean {
        if (tableNamePattern.isBlank()) return false
        return runCatching {
            databaseValue.dataSource.connection.use { connection ->
                val metaData = connection.metaData
                val resultSet = metaData.getTables(connection.catalog, connection.schema, tableNamePattern.uppercase(), tableTypes)
                resultSet.use { it.next() }
            }
        }.getOrDefault(false)
    }

}
