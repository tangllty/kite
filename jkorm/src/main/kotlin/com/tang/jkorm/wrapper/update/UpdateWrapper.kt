package com.tang.jkorm.wrapper.update

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.constants.SqlString.UPDATE
import com.tang.jkorm.mapper.BaseMapper
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.utils.Reflects

/**
 * Update wrapper for update operation
 *
 * @author Tang
 */
class UpdateWrapper<T> {

    private lateinit var table: String

    lateinit var baseMapper: BaseMapper<T>

    private lateinit var updateSetWrapper: UpdateSetWrapper<T>

    lateinit var updateWhereWrapper: UpdateWhereWrapper<T>

    constructor()

    constructor(baseMapper: BaseMapper<T>) {
        this.baseMapper = baseMapper
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
        this.updateSetWrapper = UpdateSetWrapper(this)
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

    /**
     * Get the SQL statement
     *
     * @return SqlStatement
     */
    fun getSqlStatement(): SqlStatement {
        checkValues()
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        sql.append("$UPDATE$table")
        updateSetWrapper.appendSql(sql, parameters)
        updateWhereWrapper.appendSql(sql, parameters)
        return SqlStatement(JkOrmConfig.INSTANCE.getSql(sql), parameters)
    }

    /**
     * Check the values
     */
    fun checkValues() {
        if (!this::table.isInitialized) {
            throw IllegalArgumentException("Table name is not set")
        }
    }

}
