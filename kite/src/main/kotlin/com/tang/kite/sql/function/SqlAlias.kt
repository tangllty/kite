package com.tang.kite.sql.function

import com.tang.kite.function.SFunction
import com.tang.kite.utils.Reflects.getColumnName
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
class SqlAlias internal constructor(private val column: String) {

    constructor(column: KMutableProperty1<*, *>) : this(getColumnName(column))

    constructor(column: SFunction<*, *>) : this(getColumnName(column))

    fun `as`(alias: String): String = "$column as $alias"

    fun <T> `as`(column: KMutableProperty1<T, *>): String {
        return `as`(getColumnName(column))
    }

    fun <T> `as`(column: SFunction<T, *>): String {
        return `as`(getColumnName(column))
    }

}

infix fun <T> KMutableProperty1<T, *>.`as`(alias: String): String {
    return SqlAlias(this).`as`(alias)
}

infix fun <T> KMutableProperty1<T, *>.`as`(alias: KMutableProperty1<T, *>): String {
    return SqlAlias(this).`as`(alias)
}

infix fun <T> SFunction<T, *>.`as`(alias: String): String {
    return SqlAlias(this).`as`(alias)
}

infix fun <T> SFunction<T, *>.`as`(alias: SFunction<T, *>): String {
    return SqlAlias(this).`as`(alias)
}
