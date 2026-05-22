package com.tang.kite.schema

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Index
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.config.schema.SchemaConfig.getSql
import com.tang.kite.enumeration.IndexOrder
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.metadata.IndexMeta
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.ast.TableConstraint
import com.tang.kite.sql.datatype.DataType
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.Date
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findAnnotations

/**
 * Schema Builder that generates SQL AST nodes from entity classes
 *
 * @author Tang
 */
object SchemaBuilder {

    /**
     * Build entity class and generate CREATE TABLE DDL node
     */
    @JvmStatic
    fun buildEntity(entityClass: KClass<*>): SqlNode.CreateTable {
        val tableAnnotation = entityClass.findAnnotation<Table>()
        val tableName = getSql(Reflects.getTableName(entityClass.java))

        val columns = mutableListOf<ColumnMeta>()
        val constraints = mutableListOf<TableConstraint>()
        val createIndexes = mutableListOf<SqlNode.CreateIndex>()
        val tableComment = tableAnnotation?.comment

        if (entityClass.java.isAnnotationPresent(CompositeIndex::class.java)) {
            val compositeIndexes = entityClass.java.getAnnotationsByType(CompositeIndex::class.java)
            compositeIndexes.forEach { compositeIndex ->
                val columns = compositeIndex.columns.map { getSql(it) }
                if (compositeIndex.unique) {
                    createIndexes.add(
                        SqlNode.CreateIndex(
                            indexName = "${getSql("uk")}_${tableName}_${columns.joinToString("_")}",
                            columns = columns.toList(),
                            table = TableReference(entityClass, SchemaConfig.sqlLowercase),
                            unique = true
                        )
                    )
                } else {
                    createIndexes.add(
                        SqlNode.CreateIndex(
                            indexName = "${getSql("idx")}_${tableName}_${columns.joinToString("_")}",
                            table = TableReference(entityClass, SchemaConfig.sqlLowercase),
                            columns = columns.toList()
                        )
                    )
                }
            }
        }
        Reflects.getSqlFields(entityClass.java).forEach { field ->
            val idAnnotation = field.getAnnotation(Id::class.java)
            val columnAnnotation = field.getAnnotation(Column::class.java)
            val columnMeta = createColumnMeta(field, idAnnotation, columnAnnotation)
            columns.add(columnMeta)
            val columnName = getSql(Reflects.getColumnName(field))
            if (field.isAnnotationPresent(Id::class.java)) {
                constraints.add(
                    TableConstraint.PrimaryKey(
                        columns = listOf(columnName),
                        name = "${getSql("pk")}_${tableName}_$columnName"
                    )
                )
            }
            if (field.isAnnotationPresent(Column::class.java)) {
                if (columnAnnotation.foreignKey.isNotEmpty()) {
                    constraints.add(
                        TableConstraint.ForeignKey(
                            columns = listOf(columnName),
                            referencedTable = columnAnnotation.foreignKey,
                            referencedColumns = listOf(columnAnnotation.foreignKeyColumn.ifEmpty { "id" }),
                            name = "${getSql("fk")}_${tableName}_$columnName",
                            onDelete = columnAnnotation.onDelete,
                            onUpdate = columnAnnotation.onUpdate
                        )
                    )
                }
            }
            if (field.isAnnotationPresent(Index::class.java)) {
                val indexArray = field.getAnnotationsByType(Index::class.java)
                indexArray.forEach { index ->
                    if (index.unique) {
                        createIndexes.add(
                            SqlNode.CreateIndex(
                                indexName = "${getSql("uk")}_${tableName}_$columnName",
                                table = TableReference(entityClass, SchemaConfig.sqlLowercase),
                                columns = listOf(columnName),
                                unique = true
                            )
                        )
                    } else {
                        createIndexes.add(
                            SqlNode.CreateIndex(
                                indexName = "${getSql("idx")}_${tableName}_$columnName",
                                table = TableReference(entityClass, SchemaConfig.sqlLowercase),
                                columns = listOf(columnName)
                            )
                        )
                    }
                }
            }
        }
        return SqlNode.CreateTable(
            table = TableReference(entityClass, SchemaConfig.sqlLowercase),
            columns = columns,
            constraints = constraints,
            createIndexes = createIndexes,
            comment = tableComment
        )
    }

    /**
     * Create column definition from @Column annotation
     */
    private fun createColumnMeta(field: Field, id: Id?, column: Column?): ColumnMeta {
        val typeName = getSql((column?.dataType ?: "").ifBlank { getDataType(field) })
        val columnSize = if (Number::class.java.isAssignableFrom(field.type)) column?.precision ?: -1 else column?.length ?: -1
        if (id != null) {
            return createIdColumnMeta(field, id, column, typeName, columnSize)
        }
        val tableName = getSql(Reflects.getTableName(field.declaringClass))
        val columnName = getSql(Reflects.getColumnName(field))
        if (column == null) {
            return ColumnMeta(
                tableName = tableName,
                columnName = columnName,
                typeName = typeName,
                columnSize = columnSize
            )
        }
        return ColumnMeta(
            tableName = tableName,
            columnName = columnName,
            typeName = typeName,
            columnSize = columnSize,
            decimalDigits = column.scale,
            nullable = column.nullable,
            defaultValue = column.defaultValue.firstOrNull(),
            comment = column.comment.firstOrNull()
        )
    }

    private fun createIdColumnMeta(field: Field, id: Id, column: Column?, typeName: String, columnSize: Int): ColumnMeta {
        val tableName = getSql(Reflects.getTableName(field.declaringClass))
        val columnName = getSql(Reflects.getColumnName(field))
        if (column == null) {
            return ColumnMeta(
                tableName = tableName,
                columnName = columnName,
                typeName = typeName,
                columnSize = columnSize,
                nullable = false
            )
        }
        return ColumnMeta(
            tableName = tableName,
            columnName = columnName,
            typeName = typeName,
            columnSize = columnSize,
            decimalDigits = column.scale,
            nullable = false,
            defaultValue = column.defaultValue.firstOrNull(),
            comment = column.comment.firstOrNull(),
            primaryKey = true,
            unique = true,
            autoIncrement = id.type == IdType.AUTO
        )
    }

    private fun getDataType(field: Field): String {
        return when (field.type) {
            String::class.java -> DataType.VARCHAR
            Int::class.java, Int::class.javaObjectType -> DataType.INT
            Long::class.java, Long::class.javaObjectType -> DataType.BIGINT
            Byte::class.java, Byte::class.javaObjectType -> DataType.TINYINT
            Short::class.java, Short::class.javaObjectType -> DataType.SMALLINT
            Float::class.java, Float::class.javaObjectType -> DataType.FLOAT
            Double::class.java, Double::class.javaObjectType -> DataType.DOUBLE_PRECISION
            Boolean::class.java, Boolean::class.javaObjectType -> DataType.BOOLEAN
            BigInteger::class.java -> DataType.BIGINT
            BigDecimal::class.java -> DataType.DECIMAL
            LocalTime::class.java -> DataType.TIME
            LocalDate::class.java -> DataType.DATE
            LocalDateTime::class.java, Date::class.java, Timestamp::class.java,
            OffsetDateTime::class.java, ZonedDateTime::class.java -> DataType.TIMESTAMP
            else -> DataType.VARCHAR
        }
    }

    /**
     * Build entity columns metadata for schema synchronization
     */
    @JvmStatic
    fun buildColumns(entityClass: KClass<*>): List<ColumnMeta> {
        val columns = mutableListOf<ColumnMeta>()
        Reflects.getSqlFields(entityClass.java).forEach { field ->
            val idAnnotation = field.getAnnotation(Id::class.java)
            val columnAnnotation = field.getAnnotation(Column::class.java)
            val columnMeta = createColumnMeta(field, idAnnotation, columnAnnotation)
            columns.add(columnMeta)
        }
        return columns
    }

    /**
     * Build entity indexes metadata for schema synchronization
     */
    @JvmStatic
    fun buildIndexes(entityClass: KClass<*>): List<IndexMeta> {
        val tableName = getSql(Reflects.getTableName(entityClass.java))
        val indexes = mutableListOf<IndexMeta>()
        val compositeIndexes = entityClass.findAnnotations<CompositeIndex>()

        compositeIndexes.forEach {
            val columns = it.columns.map { column -> getSql(column) }.toMutableList()
            indexes.add(createIndexMeta(tableName, it.unique, columns, it.orders.toMutableList()))
        }

        Reflects.getSqlFields(entityClass.java).forEach { field ->
            val indexAnnotations = field.getAnnotationsByType(Index::class.java)
            indexAnnotations.forEach { index ->
                val columnName = getSql(Reflects.getColumnName(field))
                indexes.add(createIndexMeta(tableName, index.unique, mutableListOf(columnName), mutableListOf(index.order)))
            }
        }
        return indexes
    }

    private fun createIndexMeta(tableName: String, unique: Boolean, columns: MutableList<String>, orders: MutableList<IndexOrder>): IndexMeta{
        val indexNamePrefix = getSql(if (unique) "uk_" else "idx_")
        return IndexMeta(
            tableName = tableName,
            indexName = "$indexNamePrefix${tableName}_${columns.joinToString("_")}",
            columns = columns,
            sorts = orders,
            unique = unique,
            primaryKey = false
        )
    }

}
