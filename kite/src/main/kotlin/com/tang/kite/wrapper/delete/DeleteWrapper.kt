package com.tang.kite.wrapper.delete

import com.tang.kite.enumeration.SqlType
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KClass

/**
 * Delete wrapper for delete operation
 *
 * @author Tang
 */
class DeleteWrapper<T : Any> : AbstractWhereWrapper<DeleteWhereWrapper<T>, T>, Wrapper<T> {

    private val sqlNode = SqlNode.Delete()

    internal lateinit var baseMapper: BaseMapper<T>

    internal lateinit var deleteWhereWrapper: DeleteWhereWrapper<T>

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
     * Set the table reference
     *
     * @param tableReference Table reference
     */
    fun from(tableReference: TableReference): DeleteWhereWrapper<T> {
        sqlNode.table = tableReference
        this.deleteWhereWrapper = DeleteWhereWrapper(this, sqlNode.where)
        return deleteWhereWrapper
    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): DeleteWhereWrapper<T> {
        return from(TableReference(table))
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun from(clazz: Class<T>): DeleteWhereWrapper<T> {
        return from(TableReference(clazz))
    }

    /**
     * Set the table name by kotlin class
     *
     * @param clazz entity class
     */
    fun from(clazz: KClass<T>): DeleteWhereWrapper<T> {
        return from(TableReference(clazz))
    }

    override fun setTableClassIfNotSet(clazz: Class<T>) {
        if (sqlNode.table != null) {
            return
        }
        sqlNode.table = TableReference(clazz)
        appendSqlNode(sqlNode.where)
    }

    override fun setTableFillFields() {
        val tableRef = sqlNode.table
        if (tableRef == null || tableRef.clazz == null) {
            return
        }
        Reflects.setTableFillFields(tableRef.clazz, SqlType.DELETE) { column, value ->
            deleteWhereWrapper.eq(column, value)
        }
    }

    override fun getSqlStatement(dialect: SqlDialect?): SqlStatement {
        return sqlNode.getSqlStatement(dialect)
    }

}
