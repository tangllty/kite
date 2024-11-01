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
class UpdateSetWrapper(val updateWrapper: UpdateWrapper) {

    private var sets = mutableMapOf<String, Any>()

    fun set(column: String, value: Any): UpdateSetWrapper {
        return set(column, value, true)
    }

    fun set(column: String, value: Any, effective: Boolean): UpdateSetWrapper {
        if (effective) {
            sets[column] = value
        }
        return this
    }

    fun <T> set(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): UpdateSetWrapper {
        return set(Reflects.getColumnName(column.javaField!!), value, effective)
    }

    fun <T> set(column: KMutableProperty1<T, *>, value: Any): UpdateSetWrapper {
        return set(column, value, true)
    }

    fun <T> set(column: SFunction<T, *>, value: Any, effective: Boolean): UpdateSetWrapper {
        return set(Reflects.getColumnName(Fields.getField(column)), value, effective)
    }

    fun <T> set(column: SFunction<T, *>, value: Any): UpdateSetWrapper {
        return set(column, value, true)
    }

    fun where(): UpdateWhereWrapper {
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
