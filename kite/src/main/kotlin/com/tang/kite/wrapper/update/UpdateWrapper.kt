package com.tang.kite.wrapper.update

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.UPDATE
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.sql.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.statement.LogicalStatement

/**
 * Update wrapper for update operation
 *
 * @author Tang
 */
class UpdateWrapper<T> : UpdateSetWrapper<T>, Wrapper<T> {

    private lateinit var table: String

    lateinit var baseMapper: BaseMapper<T>

    lateinit var updateSetWrapper: UpdateSetWrapper<T>

    lateinit var updateWhereWrapper: UpdateWhereWrapper<T>

    constructor()

    constructor(baseMapper: BaseMapper<T>) {
        this.baseMapper = baseMapper
    }

    override fun initialize(conditions: MutableList<LogicalStatement>) {
        updateWrapper = this
        super.initialize(conditions)
    }

    companion object {

        /**
         * Create a new UpdateWrapper instance
         *
         * @return UpdateWrapper
         */
        @JvmStatic
        fun <T> create(): UpdateWrapper<T> {
            return UpdateWrapper()
        }

    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): UpdateSetWrapper<T> {
        this.table = table
        this.updateSetWrapper = UpdateSetWrapper()
        this.updateSetWrapper.updateWrapper = this
        return updateSetWrapper
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun from(clazz: Class<T>): UpdateSetWrapper<T> {
        return from(Reflects.getTableName(clazz))
    }

    fun setTableClassIfNotSet(clazz: Class<T>) {
        if (::table.isInitialized) {
            return
        }
        this.table = Reflects.getTableName(clazz)
    }

    /**
     * Get the SQL statement
     *
     * @return SqlStatement
     */
    override fun getSqlStatement(): SqlStatement {
        checkValues()
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        sql.append("$UPDATE$table")
        updateSetWrapper.appendSql(sql, parameters)
        updateWhereWrapper.appendSql(sql, parameters)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    /**
     * Check the values
     */
    override fun checkValues() {
        if (!this::table.isInitialized) {
            throw IllegalArgumentException("Table name is not set")
        }
    }

}
