package com.tang.kite.wrapper.update

import com.tang.kite.enumeration.SqlType
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Wrapper
import kotlin.reflect.KClass

/**
 * Update wrapper for update operation
 *
 * @author Tang
 */
class UpdateWrapper<T : Any> : UpdateSetWrapper<T>, Wrapper<T> {

    private val sqlNode = SqlNode.Update()

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
        fun <T : Any> create(): UpdateWrapper<T> {
            return UpdateWrapper()
        }

    }

    /**
     * Set the table reference
     *
     * @param tableReference Table reference
     */
    fun from(tableReference: TableReference): UpdateSetWrapper<T> {
        sqlNode.table = tableReference
        this.updateSetWrapper = UpdateSetWrapper()
        this.updateSetWrapper.updateWrapper = this
        return updateSetWrapper
    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): UpdateSetWrapper<T> {
        return from(TableReference(table))
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun from(clazz: Class<T>): UpdateSetWrapper<T> {
        return from(TableReference(clazz))
    }

    /**
     * Set the table name by kotlin class
     *
     * @param clazz entity class
     */
    fun from(clazz: KClass<T>): UpdateSetWrapper<T> {
        return from(TableReference(clazz))
    }

    override fun setTableClassIfNotSet(clazz: Class<T>) {
        if (sqlNode.table != null) {
            return
        }
        sqlNode.table = TableReference(clazz)
        appendSqlNode(sqlNode.sets)
    }

    override fun setTableFillFields() {
        val tableRef = sqlNode.table
        if (tableRef == null || tableRef.clazz == null) {
            return
        }
        Reflects.setTableFillFields(tableRef.clazz, SqlType.UPDATE) { column, value ->
            updateSetWrapper.set(column, value)
        }
    }

    override fun getSqlStatement(dialect: SqlDialect?): SqlStatement {
        updateSetWrapper.appendSqlNode(sqlNode.sets)
        updateWhereWrapper.appendSqlNode(sqlNode.where)
        return sqlNode.getSqlStatement(dialect)
    }

}
