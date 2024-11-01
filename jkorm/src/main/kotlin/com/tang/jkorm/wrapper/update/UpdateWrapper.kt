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
class UpdateWrapper {

    private lateinit var table: String

    lateinit var baseMapper: BaseMapper<*>

    private lateinit var updateSetWrapper: UpdateSetWrapper

    lateinit var updateWhereWrapper: UpdateWhereWrapper

    constructor()

    constructor(baseMapper: BaseMapper<*>) {
        this.baseMapper = baseMapper
    }

    companion object {

        @JvmStatic
        fun create(): UpdateWrapper {
            return UpdateWrapper()
        }

    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): UpdateSetWrapper {
        this.table = table
        this.updateSetWrapper = UpdateSetWrapper(this)
        return updateSetWrapper
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun <T> from(clazz: Class<T>): UpdateSetWrapper {
        return from(Reflects.getTableName(clazz))
    }

    fun getSqlStatement(): SqlStatement {
        checkValues()
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        sql.append("$UPDATE$table")
        updateSetWrapper.appendSql(sql, parameters)
        updateWhereWrapper.appendSql(sql, parameters)
        return SqlStatement(JkOrmConfig.INSTANCE.getSql(sql), parameters)
    }

    fun checkValues() {
        if (!this::table.isInitialized) {
            throw IllegalArgumentException("Table name is not set")
        }
    }

}
