package com.tang.kite.wrapper.update

import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.SET
import com.tang.kite.function.SFunction
import com.tang.kite.utils.Fields
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Update set wrapper for update operation
 *
 * @author Tang
 */
open class UpdateSetWrapper<T>() : AbstractWhereWrapper<UpdateWhereWrapper<T>, T>() {

    lateinit var updateWrapper: UpdateWrapper<T>

    private var sets = mutableMapOf<String, Any>()

    override fun initialize(conditions: MutableList<LogicalStatement>) {
        this.updateWrapper.updateSetWrapper = this
        this.updateWrapper.updateWhereWrapper = UpdateWhereWrapper(updateWrapper, conditions)
        this.conditionInstance = updateWrapper.updateWhereWrapper
    }

    /**
     * Set the value
     *
     * @param column column name
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: String, value: Any): UpdateSetWrapper<T> {
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
    fun set(column: String, value: Any, effective: Boolean): UpdateSetWrapper<T> {
        if (effective) {
            sets[column] = value
        }
        return this
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): UpdateSetWrapper<T> {
        return set(Reflects.getColumnName(column.javaField!!), value, effective)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: KMutableProperty1<T, *>, value: Any): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: SFunction<T, *>, value: Any, effective: Boolean): UpdateSetWrapper<T> {
        return set(Reflects.getColumnName(Fields.getField(column)), value, effective)
    }

    /**
     * Set the value
     *
     * @param column column property
     * @param value value
     * @return UpdateSetWrapper
     */
    fun set(column: SFunction<T, *>, value: Any): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    /**
     * Append the SQL
     *
     * @param sql sql
     * @param parameters parameters
     */
    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        checkValues()
        val setSql = sets.entries.joinToString {
            parameters.add(it.value)
            "${it.key} = $QUESTION_MARK"
        }
        sql.append(SET + setSql)
    }

    /**
     * Check the values
     */
    open fun checkValues() {
        if (sets.isEmpty()) {
            throw IllegalArgumentException("Set value is not set")
        }
    }

}
