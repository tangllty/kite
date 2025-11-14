package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.SqlStatement
import java.lang.reflect.Field

/**
 * @author Tang
 */
class SqlNodeProvider : SqlProvider {

    override fun providerType(): DatabaseType {
        TODO("Not yet implemented")
    }

    override fun selectiveStrategy(any: Any?): Boolean {
        TODO("Not yet implemented")
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

    override fun insert(entity: Any): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun insertSelective(entity: Any): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun insertValues(entities: Iterable<Any>): SqlStatement {
        TODO("Not yet implemented")
    }

    override fun batchInsert(entities: Iterable<Any>): BatchSqlStatement {
        TODO("Not yet implemented")
    }

    override fun batchInsertSelective(entities: Iterable<Any>): List<SqlStatement> {
        TODO("Not yet implemented")
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
