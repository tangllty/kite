package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.config.KiteConfig
import com.tang.kite.constants.SqlString.DOT
import com.tang.kite.enumeration.ColumnOperator
import com.tang.kite.enumeration.SqlType
import com.tang.kite.optimistic.OptimisticLockContext
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.Column
import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.sql.statement.SubqueryStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getIdField
import com.tang.kite.utils.Reflects.getSqlFields
import com.tang.kite.utils.Reflects.getTableAlias
import com.tang.kite.utils.Reflects.getValue
import com.tang.kite.utils.Reflects.isAutoIncrementId
import java.lang.reflect.Field

/**
 * @author Tang
 */
class SqlNodeProvider(private val dialect: SqlDialect) : SqlProvider {

    private fun selectiveStrategy(any: Any?): Boolean {
        return KiteConfig.selectiveStrategy.isSelective(any)
    }

    override fun getWhere(fields: List<Field>, entity: Any, sqlType: SqlType?): List<LogicalStatement> {
        val valueMap = getSqlValues(fields, entity, sqlType)
        return fields.mapNotNull { field ->
            val value = valueMap[field]
            if (selectiveStrategy(value)) {
                val column = field.getAnnotation(com.tang.kite.annotation.Column::class.java)
                val operator = column?.operator
                val comparisonOperator = operator?.comparisonOperator ?: return@mapNotNull LogicalStatement(
                    ComparisonStatement(Column(field), value), LogicalOperator.AND
                )
                val processedValue = when (operator) {
                    ColumnOperator.LIKE, ColumnOperator.NOT_LIKE -> "%$value%"
                    ColumnOperator.LEFT_LIKE, ColumnOperator.NOT_LEFT_LIKE -> "%$value"
                    ColumnOperator.RIGHT_LIKE, ColumnOperator.NOT_RIGHT_LIKE -> "$value%"
                    else -> value
                }
                LogicalStatement(ComparisonStatement(Column(field), processedValue, comparisonOperator), LogicalOperator.AND)
            } else null
        }
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
            throw IllegalArgumentException("Only one entity can be selective inserted")
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

    private fun updateById(entity: Any, isSelective: Boolean = false): SqlStatement {
        val sqlNode = SqlNode.Update()
        val clazz = entity::class.java
        val idField = getIdField(clazz)
        sqlNode.table = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        val valueMap = getSqlValues(fieldList, entity, SqlType.UPDATE)
        val updateFieldList = fieldList.filter { it != idField && (isSelective.not() || selectiveStrategy(valueMap[it])) }
        updateFieldList.forEach { sqlNode.sets[Column(it)] = valueMap[it] }
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), valueMap[idField]), LogicalOperator.AND))
        if (OptimisticLockContext.shouldApplyOptimisticLock(sqlNode.table?.clazz!!)) {
            val versionField = Reflects.getVersionField(clazz)
            val comparisonStatement = ComparisonStatement(Column(versionField), valueMap[versionField])
            sqlNode.where.add(LogicalStatement(comparisonStatement, LogicalOperator.AND))
        }
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
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), null), LogicalOperator.AND))
        return sqlNode.getBatchSqlStatement()
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
        sqlNode.where.add(LogicalStatement(ComparisonStatement(Column(idField), ids, ComparisonOperator.IN), LogicalOperator.AND))
        return sqlNode.getSqlStatement()
    }

    private fun <T> selectOrPaginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, limitClause: LimitClause? = null): SqlStatement {
        val sqlNode = SqlNode.Select()
        sqlNode.from = TableReference(clazz)
        val fieldList = getSqlFields(clazz)
        fieldList.forEach { sqlNode.columns.add(Column(it)) }

        if (entity != null) {
            sqlNode.where.addAll(getWhere(fieldList, entity, SqlType.SELECT))
        }

        if (orderBys.isNotEmpty()) {
            sqlNode.orderBy.addAll(orderBys)
        }
        sqlNode.limit = limitClause
        return sqlNode.getSqlStatement(dialect)
    }

    override fun <T> select(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>): SqlStatement {
        return selectOrPaginate(clazz, entity, orderBys)
    }

    private fun <T> selectOrPaginateWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, limitClause: LimitClause? = null): SqlNode.Select {
        val sqlNode = SqlNode.Select()
        val joins = Reflects.getJoins(clazz)
        val tableAlias = getTableAlias(clazz)
        val sqlFields = mutableListOf<Field>().apply {
            addAll(getSqlFields(clazz))
            joins.forEach { addAll(getSqlFields(it.type)) }
        }
        sqlNode.columns.addAll(sqlFields.map { Column(it) })
        sqlNode.from = TableReference(clazz)
        joins.forEach {
            val joinTableAlias = getTableAlias(it.type)
            val join = it.getAnnotation(Join::class.java)!!
            val selfField = join.selfField
            val targetField = join.targetField
            val joinTable = join.joinTable
            val joinSelfField = join.joinSelfColumn
            val joinTargetField = join.joinTargetColumn
            if (joinTable.isNotEmpty() && joinSelfField.isNotEmpty() && joinTargetField.isNotEmpty()) {
                val innerJoinTableAlias = joinTable.split("_").joinToString("") { split -> split.first().toString() }
                val innerConditions = mutableListOf<LogicalStatement>()
                val innerCondition = ComparisonStatement(Column(joinSelfField, innerJoinTableAlias), tableAlias + DOT + selfField)
                innerConditions.add(LogicalStatement(innerCondition, LogicalOperator.AND))
                sqlNode.joins.add(JoinTable(TableReference(joinTable, innerJoinTableAlias), JoinType.LEFT, innerConditions))

                val conditions = mutableListOf<LogicalStatement>()
                val condition = ComparisonStatement(Column(targetField, joinTableAlias), innerJoinTableAlias + DOT + joinTargetField)
                conditions.add(LogicalStatement(condition, LogicalOperator.AND))
                sqlNode.joins.add(JoinTable(TableReference(it.type), JoinType.LEFT, conditions))
            } else {
                val conditions = mutableListOf<LogicalStatement>()
                val condition = ComparisonStatement(Column(selfField, tableAlias), Column(targetField, joinTableAlias))
                conditions.add(LogicalStatement(condition, LogicalOperator.AND))
                sqlNode.joins.add(JoinTable(TableReference(it.type), JoinType.LEFT, conditions))
            }
        }
        if (entity != null) {
            val whereSqlFields = getSqlFields(clazz).filter { selectiveStrategy(it) }
            sqlNode.where.addAll(getWhere(whereSqlFields, entity, SqlType.SELECT))
        }
        if (orderBys.isNotEmpty()) {
            sqlNode.orderBy.addAll(orderBys)
        }
        sqlNode.limit = limitClause
        return sqlNode
    }

    override fun <T> selectWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, withAlias: Boolean): SqlStatement {
        return selectOrPaginateWithJoins(clazz, entity, orderBys).getSqlStatement(dialect)
    }

    override fun <T> populateJoins(join: Field, type: Class<T>, entity: Any, joinType: Class<*>): SqlStatement {
        val joinAnnotation = join.getAnnotation(Join::class.java)!!
        val joinTable = joinAnnotation.joinTable
        val joinSelfField = joinAnnotation.joinSelfColumn
        val joinTargetField = joinAnnotation.joinTargetColumn
        val selfField = Reflects.getField(type, joinAnnotation.selfField)
        Reflects.makeAccessible(selfField!!, entity)
        val selfFieldValue = getValue(selfField, entity)
        val joinSelect = selectOrPaginateWithJoins(joinType, null, emptyArray())
        val joinSqlNode = if (joinTable.isNotEmpty() && joinSelfField.isNotEmpty() && joinTargetField.isNotEmpty()) {
            getNestedSelect(joinSelect, joinAnnotation.targetField, selfFieldValue, joinAnnotation)
        } else {
            getInCondition(joinSelect, joinAnnotation.targetField, listOf(selfFieldValue))
        }
        return joinSqlNode.getSqlStatement(dialect)
    }

    private fun getNestedSelect(sqlNode: SqlNode.Select, field: String, value: Any?, join: Join): SqlNode.Select {
        val subqueryNode = SqlNode.Select()
        subqueryNode.columns.add(Column(join.joinTargetColumn))
        subqueryNode.from = TableReference(join.joinTable)
        val comparisonStatement = ComparisonStatement(Column(join.joinSelfColumn), value)
        subqueryNode.where.add(LogicalStatement(comparisonStatement, LogicalOperator.AND))
        val subqueryStatement = SubqueryStatement(subqueryNode, ComparisonOperator.IN, dialect)
        val subqueryComparisonStatement = ComparisonStatement(Column(field), subqueryStatement, ComparisonOperator.SUBQUERY)
        sqlNode.where.add(LogicalStatement(subqueryComparisonStatement, LogicalOperator.AND))
        return sqlNode
    }

    private fun getInCondition(sqlNode: SqlNode.Select, field: String, values: Iterable<Any?>): SqlNode.Select {
        val comparisonStatement = ComparisonStatement(Column(field), values, ComparisonOperator.IN)
        sqlNode.where.add(LogicalStatement(comparisonStatement, LogicalOperator.AND))
        return sqlNode
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
        return selectOrPaginate(clazz, entity, orderBys, LimitClause(pageNumber, pageSize))
    }

    override fun <T> paginateWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long, withAlias: Boolean): SqlStatement {
        return selectOrPaginateWithJoins(clazz, entity, orderBys, LimitClause(pageNumber, pageSize)).getSqlStatement(dialect)
    }

}
