package com.tang.kite.wrapper.update

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KMutableProperty1

/**
 * Update set wrapper for update operation
 *
 * @author Tang
 */
open class UpdateSetWrapper<T : Any> : AbstractWhereWrapper<UpdateWhereWrapper<T>, T>() {

    internal lateinit var updateWrapper: UpdateWrapper<T>

    private val sets: LinkedHashMap<Column, Any?> = linkedMapOf()

    override fun initialize(conditions: MutableList<LogicalStatement>) {
        this.updateWrapper.updateSetWrapper = this
        this.updateWrapper.updateWhereWrapper = UpdateWhereWrapper(updateWrapper, conditions)
        this.conditionInstance = updateWrapper.updateWhereWrapper
    }

    /**
     * Set the value
     *
     * @param column column
     * @param value value
     * @param effective whether effective
     * @return UpdateSetWrapper
     */
    fun set(column: Column, value: Any?, effective: Boolean): UpdateSetWrapper<T> {
        if (effective) {
            sets[column] = value
        }
        return this
    }

    /**
     * Set the value
     *
     * @param column column
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: Column, value: Any?): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    /**
     * Set the value
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return UpdateSetWrapper
     */
    fun set(column: String, value: Any?, effective: Boolean): UpdateSetWrapper<T> {
        return set(Column(column), value, effective)
    }

    /**
     * Set the value
     *
     * @param column column name
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: String, value: Any?): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: KMutableProperty1<T, *>, value: Any?, effective: Boolean): UpdateSetWrapper<T> {
        return set(Column(column), value, effective)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: KMutableProperty1<T, *>, value: Any?): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: SFunction<T, *>, value: Any?, effective: Boolean): UpdateSetWrapper<T> {
        return set(Column(column), value, effective)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: SFunction<T, *>, value: Any?): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    fun appendSqlNode(sets: LinkedHashMap<Column, Any?>) {
        sets.putAll(this.sets)
    }

}
