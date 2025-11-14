package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.config.KiteConfig
import com.tang.kite.enumeration.SqlType
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.Column
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.statement.BatchSqlStatement
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

    override fun <T> getOrderBy(orderBys: Array<OrderItem<T>>, withAlias: Boolean): String {
        TODO("Not yet implemented")
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        TODO("Not yet implemented")
    }

    private fun insert(entities: Iterable<Any>, isSelective: Boolean = false): SqlNode.Insert {
        val sqlNode = SqlNode.Insert()
        val entity = entities.first()
        val clazz = entity::class.java
        sqlNode.table = TableReference(clazz)
        val idField = getIdField(clazz)
        val autoIncrementId = isAutoIncrementId(idField)
        val fieldList = getSqlFields(clazz).filter { it != idField || autoIncrementId.not() }
        val getValue = { field: Field, entity: Any ->
            if (field == idField && autoIncrementId.not()) {
                Reflects.getGeneratedId(idField)
            } else {
                getValue(field, entity, SqlType.INSERT)
            }
        }

        if (isSelective) {
            if (entities.count() == 1) {
                val selectiveFieldList = fieldList.filter { selectiveStrategy(getValue(it, entity, SqlType.INSERT)) }
                sqlNode.valuesList.add(mutableListOf())
                selectiveFieldList.forEach {
                        sqlNode.columns.add(Column(it))
                        sqlNode.valuesList.first().add(getValue(it, entity))
                    }
                return sqlNode
            }
            entities.forEach { entity ->
                sqlNode.columnsValuesList.add(Pair(mutableListOf(), mutableListOf()))
                val selectiveFieldList = fieldList.filter { selectiveStrategy(getValue(it, entity, SqlType.INSERT)) }
                selectiveFieldList.forEach {
                    sqlNode.columnsValuesList.last().first.add(Column(it))
                    sqlNode.columnsValuesList.last().second.add(getValue(it, entity))
                }
            }
            return sqlNode
        }

        fieldList.forEach { sqlNode.columns.add(Column(it)) }
        entities.forEach {
            sqlNode.valuesList.add(mutableListOf())
            fieldList.forEach { field ->
                sqlNode.valuesList.last().add(getValue(field, it))
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
        return insert(entities, true).getInsertSqlStatementList()
    }

    override fun update(entity: Any): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun update(entity: Any, where: Any): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun updateSelective(entity: Any): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun batchUpdate(entities: Iterable<Any>): BatchSqlStatement {
        TODO("Not yet implemented")
    }

    override fun batchUpdateSelective(entities: Iterable<Any>): List<SqlStatement> {
        TODO("Not yet implemented")
    }

    override fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> deleteByIds(clazz: Class<T>, ids: Iterable<Any>): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> select(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> selectWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, withAlias: Boolean): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun <T> paginateWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long, withAlias: Boolean): SqlStatement {
        TODO("Not yet implemented")
    }

}
