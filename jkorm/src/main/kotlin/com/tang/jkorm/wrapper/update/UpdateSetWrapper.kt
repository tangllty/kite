package com.tang.jkorm.wrapper.update

import com.tang.jkorm.constants.SqlString.QUESTION_MARK
import com.tang.jkorm.constants.SqlString.SET
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Update set wrapper for update operation
 *
 * @author Tang
 */
class UpdateSetWrapper<T>(val updateWrapper: UpdateWrapper<T>) {

    private var sets = mutableMapOf<String, Any>()

    fun set(column: String, value: Any): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    fun set(column: String, value: Any, effective: Boolean): UpdateSetWrapper<T> {
        if (effective) {
            sets[column] = value
        }
        return this
    }

    fun set(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): UpdateSetWrapper<T> {
        return set(Reflects.getColumnName(column.javaField!!), value, effective)
    }

    fun set(column: KMutableProperty1<T, *>, value: Any): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    fun set(column: SFunction<T, *>, value: Any, effective: Boolean): UpdateSetWrapper<T> {
        return set(Reflects.getColumnName(Fields.getField(column)), value, effective)
    }

    fun set(column: SFunction<T, *>, value: Any): UpdateSetWrapper<T> {
        return set(column, value, true)
    }

    fun where(): UpdateWhereWrapper<T> {
        this.updateWrapper.updateWhereWrapper = UpdateWhereWrapper(updateWrapper)
        return updateWrapper.updateWhereWrapper
    }

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        checkValues()
        val setSql = sets.entries.joinToString {
            parameters.add(it.value)
            "${it.key} = $QUESTION_MARK"
        }
        sql.append(SET + setSql)
    }

    fun checkValues() {
        if (sets.isEmpty()) {
            throw IllegalArgumentException("Set value is not set")
        }
    }

}
