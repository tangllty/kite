package com.tang.kite.schema

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Index
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.metadata.IndexMeta
import com.tang.kite.metadata.IndexStructure
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.ast.TableConstraint
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.datatype.DataType
import com.tang.kite.utils.CaseFormat
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
    fun buildEntity(entityClass: KClass<*>): SqlNode.CreateTable {
        val tableAnnotation = entityClass.findAnnotation<Table>()
        val tableName = Reflects.getTableName(entityClass.java)

        val columns = mutableListOf<ColumnMeta>()
        val constraints = mutableListOf<TableConstraint>()
        val createIndexes = mutableListOf<SqlNode.CreateIndex>()
        val tableComment = tableAnnotation?.comment

        if (entityClass.java.isAnnotationPresent(CompositeIndex::class.java)) {
            val compositeIndexes = entityClass.java.getAnnotationsByType(CompositeIndex::class.java)
            compositeIndexes.forEach { compositeIndex ->
                if (compositeIndex.unique) {
                    createIndexes.add(
                        SqlNode.CreateIndex(
                            indexName = "uk_${tableName}_${compositeIndex.columns.joinToString("_")}",
                            columns = compositeIndex.columns.toList(),
                            table = TableReference(entityClass),
                            unique = true
                        )
                    )
                } else {
                    createIndexes.add(
                        SqlNode.CreateIndex(
                            indexName = "idx_${tableName}_${compositeIndex.columns.joinToString("_")}",
                            table = TableReference(entityClass),
                            columns = compositeIndex.columns.toList()
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
            val snakeCaseFieldName = field.name.toSnakeCase()
            val columnName = columnAnnotation?.value?.ifEmpty { snakeCaseFieldName } ?: snakeCaseFieldName
            if (field.isAnnotationPresent(Id::class.java)) {
                constraints.add(
                    TableConstraint.PrimaryKey(
                        columns = listOf(columnName),
                        name = "pk_${tableName}_$columnName"
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
                            name = "fk_${tableName}_$columnName",
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
                                indexName = "uk_${tableName}_$columnName",
                                table = TableReference(entityClass),
                                columns = listOf(columnName),
                                unique = true
                            )
                        )
                    } else {
                        createIndexes.add(
                            SqlNode.CreateIndex(
                                indexName = "idx_${tableName}_$columnName",
                                table = TableReference(entityClass),
                                columns = listOf(columnName)
                            )
                        )
                    }
                }
            }
        }
        return SqlNode.CreateTable(TableReference(entityClass), columns, constraints, createIndexes, tableComment)
    }

    /**
     * Create column definition from @Column annotation
     */
    private fun createColumnMeta(field: Field, id: Id?, column: Column?): ColumnMeta {
        if (id != null) {
            return createIdColumnMeta(field, id, column)
        }
        if (column == null) {
            return ColumnMeta(
                tableName = Reflects.getTableName(field.declaringClass),
                columnName = Reflects.getColumnName(field),
                typeName = getDataType(field),
                columnSize = 0
            )
        }
        val columnSize = if (Number::class.java.isAssignableFrom(field.type)) column.precision else column.length
        return ColumnMeta(
            tableName = Reflects.getTableName(field.declaringClass),
            columnName = Reflects.getColumnName(field),
            typeName = column.dataType,
            columnSize = columnSize,
            decimalDigits = column.scale,
            nullable = column.nullable,
            defaultValue = column.defaultValue,
            comment = column.comment
        )
    }

    private fun createIdColumnMeta(field: Field, id: Id, column: Column?): ColumnMeta {
        val typeName = column?.dataType ?: getDataType(field)
        val columnSize =
            if (Number::class.java.isAssignableFrom(field.type)) column?.precision ?: 0 else column?.length ?: 0
        val columnMeta = ColumnMeta(
            tableName = Reflects.getTableName(field.declaringClass),
            columnName = Reflects.getColumnName(field),
            typeName = typeName,
            columnSize = columnSize,
            nullable = false
        )
        if (column == null) {
            return columnMeta
        }
        return ColumnMeta(
            tableName = Reflects.getTableName(field.declaringClass),
            columnName = Reflects.getColumnName(field),
            typeName = typeName,
            columnSize = columnSize,
            decimalDigits = column.scale,
            nullable = false,
            defaultValue = column.defaultValue,
            comment = column.comment,
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
     * Convert camelCase to snake_case
     */
    private fun String.toSnakeCase(): String {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this)
    }

    /**
     * Build entity columns metadata for schema synchronization
     */
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
    fun buildIndexes(entityClass: KClass<*>): List<IndexMeta> {
        val tableName = Reflects.getTableName(entityClass.java)
        val indexes = mutableListOf<IndexMeta>()
        val compositeIndexes = entityClass.findAnnotations<CompositeIndex>()

        compositeIndexes.forEach {
            indexes.add(createIndexMeta(tableName, it.unique, it.columns, it.filterCondition))
        }

        Reflects.getSqlFields(entityClass.java).forEach { field ->
            val indexAnnotations = field.getAnnotationsByType(Index::class.java)
            indexAnnotations.forEach { index ->
                val columnName = Reflects.getColumnName(field)
                indexes.add(createIndexMeta(tableName, index.unique, arrayOf(columnName), index.filterCondition))
            }
        }
        return indexes
    }

    private fun createIndexMeta(tableName: String, unique: Boolean, columns: Array<String>, filterCondition: String): IndexMeta{
        val indexNamePrefix = if (unique) "uk_" else "idx_"
        return IndexMeta(
            tableName = tableName,
            indexName = "$indexNamePrefix${tableName}_${columns.joinToString("_")}",
            unique = unique,
            indexStructure = if (unique) IndexStructure.BTREE else IndexStructure.HASH,
            cardinality = 0L,
            pages = 0L,
            filterCondition = filterCondition,
            isPrimaryKey = false
        )
    }

}
