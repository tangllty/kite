package com.tang.kite.wrapper.delete

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.DELETE_FROM
import com.tang.kite.enumeration.SqlType
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KClass

/**
 * Delete wrapper for delete operation
 *
 * @author Tang
 */
class DeleteWrapper<T : Any> : AbstractWhereWrapper<DeleteWhereWrapper<T>, T>, Wrapper<T> {

    private lateinit var tableClass: Class<T>

    private lateinit var table: String

    lateinit var baseMapper: BaseMapper<T>

    lateinit var deleteWhereWrapper: DeleteWhereWrapper<T>

    constructor()

    constructor(baseMapper: BaseMapper<T>) {
        this.baseMapper = baseMapper
    }

    override fun initialize(conditions: MutableList<LogicalStatement>) {
        deleteWhereWrapper = DeleteWhereWrapper(this, conditions)
        this.conditionInstance = deleteWhereWrapper
    }

    companion object {

        /**
         * Create a new DeleteWrapper instance
         *
         * @return DeleteWrapper
         */
        @JvmStatic
        fun <T : Any> create(): DeleteWrapper<T> {
            return DeleteWrapper()
        }
    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): DeleteWhereWrapper<T> {
        this.table = table
        this.deleteWhereWrapper = DeleteWhereWrapper(this, mutableListOf())
        return deleteWhereWrapper
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun from(clazz: Class<T>): DeleteWhereWrapper<T> {
        return from(Reflects.getTableName(clazz))
    }

    /**
     * Set the table name by kotlin class
     *
     * @param clazz entity class
     */
    fun from(clazz: KClass<T>): DeleteWhereWrapper<T> {
        return from(clazz.java)
    }

    fun setTableClassIfNotSet(clazz: Class<T>) {
        if (::tableClass.isInitialized) {
            return
        }
        this.tableClass = clazz
        this.table = Reflects.getTableName(clazz)
    }

    fun setTableFillFields() {
        Reflects.setTableFillFields(tableClass, SqlType.DELETE) { column, value ->
            deleteWhereWrapper.eq(column, value)
        }
    }

    override fun getSqlStatement(): SqlStatement {
        checkValues()
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        sql.append("$DELETE_FROM$table")
        deleteWhereWrapper.appendSql(sql, parameters)
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
