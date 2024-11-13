package com.tang.jkorm.wrapper.query

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.constants.SqlString.SELECT
import com.tang.jkorm.constants.SqlString.SELECT_DISTINCT
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.mapper.BaseMapper
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import com.tang.jkorm.wrapper.Wrapper
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Build a select query
 *
 * @author Tang
 */
class QueryWrapper<T> : Wrapper<T> {

    private var distinct: Boolean = false

    lateinit var baseMapper: BaseMapper<T>

    private lateinit var querySelectWrapper: QuerySelectWrapper<T>

    lateinit var queryWhereWrapper: QueryWhereWrapper<T>

    constructor()

    constructor(baseMapper: BaseMapper<T>) {
        this.baseMapper = baseMapper
    }

    companion object {

        /**
         * Create a new QueryWrapper instance
         */
        @JvmStatic
        fun <T> create(): QueryWrapper<T> {
            return QueryWrapper()
        }

    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun select(vararg columns: String): QuerySelectWrapper<T> {
        this.querySelectWrapper = QuerySelectWrapper(this, columns.toMutableList())
        return querySelectWrapper
    }

    /**
     * Select without columns
     *
     * @return QuerySelectWrapper
     */
    fun select(): QuerySelectWrapper<T> {
        return select(*mutableListOf<String>().toTypedArray())
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun select(vararg columns: SFunction<T, *>): QuerySelectWrapper<T> {
        val columnNames = columns.map { Reflects.getColumnName(Fields.getField(it)) }
        return select(*columnNames.toTypedArray())
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun select(vararg columns: KMutableProperty1<T, *>): QuerySelectWrapper<T> {
        val columnNames = columns.map { Reflects.getColumnName(it.javaField!!) }
        return select(*columnNames.toTypedArray())
    }

    /**
     * Make the select columns distinct
     *
     * @return QueryWrapper
     */
    fun distinct(): QueryWrapper<T> {
        this.distinct = true
        return this
    }

    override fun getSqlStatement(): SqlStatement {
        checkValues()
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        sql.append(if (distinct) SELECT_DISTINCT else SELECT)
        querySelectWrapper.appendSql(sql)
        queryWhereWrapper.appendSql(sql, parameters)
        return SqlStatement(JkOrmConfig.INSTANCE.getSql(sql), parameters)
    }

    private fun checkValues() {
    }

}
