package com.tang.kite.wrapper.query

import com.tang.kite.enumeration.SqlType
import com.tang.kite.function.SFunction
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.sql.Column
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KMutableProperty1

/**
 * Build a select query
 *
 * @author Tang
 */
class QueryWrapper<T : Any> : AbstractWhereWrapper<QueryWhereWrapper<T>, T>, Wrapper<T> {

    private val sqlNode = SqlNode.Select()

    lateinit var baseMapper: BaseMapper<T>

    lateinit var querySelectWrapper: QuerySelectWrapper<T>

    lateinit var queryWhereWrapper: QueryWhereWrapper<T>

    constructor()

    constructor(baseMapper: BaseMapper<T>) {
        this.baseMapper = baseMapper
    }

    override fun initialize(conditions: MutableList<LogicalStatement>) {
        querySelectWrapper = QuerySelectWrapper(this, sqlNode)
        queryWhereWrapper = QueryWhereWrapper(this, sqlNode.where)
        this.conditionInstance = queryWhereWrapper
    }

    companion object {

        /**
         * Create a new QueryWrapper instance
         */
        @JvmStatic
        fun <T : Any> create(): QueryWrapper<T> {
            return QueryWrapper()
        }

    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun select(vararg columns: Column): QuerySelectWrapper<T> {
        sqlNode.columns.addAll(columns)
        this.querySelectWrapper = QuerySelectWrapper(this, sqlNode)
        return querySelectWrapper
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun select(vararg columns: String): QuerySelectWrapper<T> {
        return select(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Select without columns
     *
     * @return QuerySelectWrapper
     */
    fun select(): QuerySelectWrapper<T> {
        return select(*mutableListOf<Column>().toTypedArray())
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun select(vararg columns: SFunction<T, *>): QuerySelectWrapper<T> {
        return select(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun select(vararg columns: KMutableProperty1<T, *>): QuerySelectWrapper<T> {
        return select(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Make the select columns distinct
     *
     * @return QueryWrapper
     */
    fun distinct(): QueryWrapper<T> {
        sqlNode.distinct = true
        return this
    }

    override fun setTableClassIfNotSet(clazz: Class<T>) {
        if (sqlNode.from != null) {
            return
        }
        sqlNode.from = TableReference(clazz)
        appendSqlNode(sqlNode.where)
    }

    override fun setTableFillFields() {
        val tableRef = sqlNode.from
        if (tableRef == null || tableRef.clazz == null) {
            return
        }
        Reflects.setTableFillFields(tableRef.clazz, SqlType.SELECT) { column, value ->
            queryWhereWrapper.eq(column, value)
        }
    }

    override fun getSqlStatement(dialect: SqlDialect?): SqlStatement {
        queryWhereWrapper.appendSqlNode(sqlNode)
        return sqlNode.getSqlStatement(dialect)
    }

}
