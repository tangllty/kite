package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.config.KiteConfig
import com.tang.kite.enumeration.SqlType
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.Column
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getIdField
import com.tang.kite.utils.Reflects.getSqlFields
import com.tang.kite.utils.Reflects.getValue
import com.tang.kite.utils.Reflects.isAutoIncrementId
import java.lang.reflect.Field

/**
 * @author Tang
 */
class SqlNodeProvider(private val dialect: SqlDialect) : SqlProvider {

    override fun providerType(): DatabaseType {
        TODO("Not yet implemented")
    }

    override fun selectiveStrategy(any: Any?): Boolean {
        return KiteConfig.selectiveStrategy.apply(any)
    }

    override fun getSql(sql: StringBuilder): String {
        TODO("Not yet implemented")
    }

    override fun getInCondition(sql: String, field: String, values: Iterable<Any?>, withAlias: Boolean): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun getNestedSelect(sql: String, field: String, value: Iterable<Any?>, join: Join): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun getColumns(fieldList: List<Field>, withAlias: Boolean): String {
        TODO("Not yet implemented")
    }

    override fun <T> getWhere(parameters: MutableList<Any?>, clazz: Class<T>, entity: Any?, withAlias: Boolean): String {
        TODO("Not yet implemented")
    }

    override fun getWhere(fields: List<Field>, entity: Any, sqlType: SqlType?): List<LogicalStatement> {
        val valueMap = getSqlValues(fields, entity, sqlType)
        return fields.mapNotNull { field ->
            val value = valueMap[field]
            if (selectiveStrategy(value)) LogicalStatement(ComparisonStatement(Column(field), value)) else null
        }
    }

    override fun <T> getOrderBy(orderBys: Array<OrderItem<T>>, withAlias: Boolean): String {
        TODO("Not yet implemented")
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        TODO("Not yet implemented")
    }

    private fun getSqlValues(fields: List<Field>, entity: Any, sqlType: SqlType? = null, idField: Field? = null, isAutoIncrementId: Boolean = true): Map<Field, Any?> {
        return fields.associateWith {
            if (it == idField && isAutoIncrementId.not()) {
                Reflects.getGeneratedId(it)
            } else {
                if (sqlType != null) {
                    getValue(it, entity, sqlType)
                } else {
                    getValue(it, entity)
                }
            }
        }
    }

    private fun insert(entities: Iterable<Any>, isSelective: Boolean = false): SqlNode.Insert {
        val sqlNode = SqlNode.Insert()
        val entity = entities.first()
        val clazz = entity::class.java
        sqlNode.table = TableReference(clazz)
        val idField = getIdField(clazz)
        val autoIncrementId = isAutoIncrementId(idField)
        val fieldList = getSqlFields(clazz).filter { it != idField || autoIncrementId.not() }

        if (isSelective) {
            if (entities.count() == 1) {
                val valueMap = getSqlValues(fieldList, entity, SqlType.INSERT, idField, autoIncrementId)
                val selectiveFieldList = fieldList.filter { selectiveStrategy(valueMap[it]) }
                sqlNode.valuesList.add(mutableListOf())
                selectiveFieldList.forEach {
                    sqlNode.columns.add(Column(it))
                    sqlNode.valuesList.first().add(valueMap[it])
                }
                return sqlNode
            }
            entities.forEach { entity ->
                val valueMap = getSqlValues(fieldList, entity, SqlType.INSERT, idField, autoIncrementId)
                sqlNode.columnsValuesList.add(Pair(mutableListOf(), mutableListOf()))
                val selectiveFieldList = fieldList.filter { selectiveStrategy(valueMap[it]) }
                selectiveFieldList.forEach { field ->
                    sqlNode.columnsValuesList.last().first.add(Column(field))
                    sqlNode.columnsValuesList.last().second.add(valueMap[field])
                }
            }
            return sqlNode
        }

        fieldList.forEach { sqlNode.columns.add(Column(it)) }
        entities.forEach { entity ->
            sqlNode.valuesList.add(mutableListOf())
            val valueMap = getSqlValues(fieldList, entity, SqlType.INSERT, idField, autoIncrementId)
            fieldList.forEach { field ->
                sqlNode.valuesList.last().add(valueMap[field])
            }
        }
        return sqlNode
    }

    override fun insert(entity: Any): SqlStatement {
        return insert(listOf(entity)).getSqlStatement()
    }

    override fun insertSelective(entity: Any): SqlStatement {
        return insert(listOf(entity), true).getSqlStatement()
    }

    override fun insertValues(entities: Iterable<Any>): SqlStatement {
        return insert(entities).getSqlStatement()
    }

    override fun batchInsert(entities: Iterable<Any>): BatchSqlStatement {
        return insert(entities).getBatchSqlStatement()
    }

    override fun batchInsertSelective(entities: Iterable<Any>): List<SqlStatement> {
        return insert(entities, true).getSqlStatementList()
    }

    private fun updateById(entity: Any, isSelective: Boolean = false): SqlStatement {
        val sqlNode = SqlNode.Update()
        val clazz = entity::class.java
        val idField = getIdField(clazz)
        sqlNode.table = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        val valueMap = getSqlValues(fieldList, entity, SqlType.UPDATE)
        val updateFieldList = fieldList.filter { it != idField && (isSelective.not() || selectiveStrategy(valueMap[it])) }
        updateFieldList.forEach { sqlNode.sets[Column(it)] = valueMap[it] }
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), valueMap[idField])))
        return sqlNode.getSqlStatement()
    }

    private fun updateCondition(entity: Any, where: Any, isSelective: Boolean = false): SqlStatement {
        val sqlNode = SqlNode.Update()
        val clazz = entity::class.java
        sqlNode.table = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        val valueMap = getSqlValues(fieldList, entity, SqlType.UPDATE)
        val updateFieldList = fieldList.filter { isSelective.not() || selectiveStrategy(valueMap[it]) }
        updateFieldList.forEach { sqlNode.sets[Column(it)] = valueMap[it] }
        val whereFieldList = getSqlFields(where::class.java)
        sqlNode.where.addAll(getWhere(whereFieldList, where))
        return sqlNode.getSqlStatement()
    }

    override fun update(entity: Any): SqlStatement {
        return updateById(entity)
    }

    override fun update(entity: Any, where: Any): SqlStatement {
        return updateCondition(entity, where)
    }

    override fun updateSelective(entity: Any): SqlStatement {
        return updateById(entity, true)
    }

    override fun updateSelective(entity: Any, where: Any): SqlStatement {
        return updateCondition(entity, where, true)
    }

    override fun batchUpdate(entities: Iterable<Any>): BatchSqlStatement {
        val sqlNode = SqlNode.Update()
        val clazz = entities.first()::class.java
        val idField = getIdField(clazz)
        sqlNode.table = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        val insertFieldList = fieldList.filter { it != idField }
        insertFieldList.forEach {
            sqlNode.sets[Column(it)] = null
        }
        entities.forEach { entity ->
            sqlNode.valuesList.add(mutableListOf())
            val valueMap = getSqlValues(fieldList, entity, SqlType.UPDATE)
            insertFieldList.forEach { sqlNode.valuesList.last().add(valueMap[it]) }
            sqlNode.valuesList.last().add(valueMap[idField])
        }
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), null)))
        return sqlNode.getBatchSqlStatement()
    }

    override fun batchUpdateSelective(entities: Iterable<Any>): List<SqlStatement> {
        val sqlNode = SqlNode.Update()
        val clazz = entities.first()::class.java
        val idField = getIdField(clazz)
        sqlNode.table = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        entities.forEach { entity ->
            sqlNode.setsList.add(Pair(mutableListOf(), mutableListOf()))
            val valueMap = getSqlValues(fieldList, entity, SqlType.UPDATE)
            val insertFieldList = fieldList.filter { it != idField && selectiveStrategy(valueMap[it]) }
            insertFieldList.forEach {
                sqlNode.setsList.last().first.add(Column(it))
                sqlNode.setsList.last().second.add(valueMap[it])
            }
            sqlNode.setsList.last().second.add(valueMap[idField])
        }
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), null)))
        return sqlNode.getSqlStatementList()
    }

    override fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement {
        val sqlNode = SqlNode.Delete()
        sqlNode.table = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        sqlNode.where.addAll(getWhere(fieldList, entity, SqlType.DELETE))
        return sqlNode.getSqlStatement()
    }

    override fun <T> deleteByIds(clazz: Class<T>, ids: Iterable<Any>): SqlStatement {
        val sqlNode = SqlNode.Delete()
        sqlNode.table = TableReference(clazz)
        val idField = getIdField(clazz)
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), ids, ComparisonOperator.IN)))
        return sqlNode.getSqlStatement()
    }

    override fun <T> select(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>): SqlStatement {
        val sqlNode = SqlNode.Select()
        sqlNode.from = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        fieldList.forEach { sqlNode.columns.add(Column(it)) }

        if (entity != null) {
            sqlNode.where.addAll(getWhere(fieldList, entity, SqlType.SELECT))
        }

        if (orderBys.isNotEmpty()) {
            orderBys.forEach { sqlNode.orderBy.add(it) }
        }
        return sqlNode.getSqlStatement(dialect)
    }

    override fun <T> selectWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, withAlias: Boolean): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement {
        val sqlNode = SqlNode.Select()
        sqlNode.from = TableReference(clazz)
        sqlNode.count = true
        val fieldList = getSqlFields(clazz)

        if (entity != null) {
            sqlNode.where.addAll(getWhere(fieldList, entity, SqlType.SELECT))
        }
        return sqlNode.getSqlStatement(dialect)
    }

    override fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long): SqlStatement {
        val sqlNode = SqlNode.Select()
        sqlNode.from = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        fieldList.forEach { sqlNode.columns.add(Column(it)) }

        if (entity != null) {
            sqlNode.where.addAll(getWhere(fieldList, entity, SqlType.SELECT))
        }

        if (orderBys.isNotEmpty()) {
            orderBys.forEach { sqlNode.orderBy.add(it) }
        }
        sqlNode.limit = LimitClause(pageNumber, pageSize)
        return sqlNode.getSqlStatement(dialect)
    }

    override fun <T> paginateWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long, withAlias: Boolean): SqlStatement {
        TODO("Not yet implemented")
    }

}
