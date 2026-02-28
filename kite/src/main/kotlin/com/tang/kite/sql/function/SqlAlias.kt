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

    fun alias(alias: String): String {
        return "$column as $alias"
    }

    fun <T> alias(column: KMutableProperty1<T, *>): String {
        return alias(getColumnName(column))
    }

    fun <T> alias(column: SFunction<T, *>): String {
        return alias(getColumnName(column))
    }

    fun `as`(alias: String): String {
        return alias(alias)
    }

    fun <T> `as`(column: KMutableProperty1<T, *>): String {
        return alias(column)
    }

    fun <T> `as`(column: SFunction<T, *>): String {
        return alias(column)
    }

}

infix fun <T> KMutableProperty1<T, *>.alias(alias: String): String {
    return SqlAlias(this).alias(alias)
}

infix fun <T> KMutableProperty1<T, *>.alias(alias: KMutableProperty1<T, *>): String {
    return SqlAlias(this).alias(alias)
}

infix fun <T> SFunction<T, *>.alias(alias: String): String {
    return SqlAlias(this).alias(alias)
}

infix fun <T> SFunction<T, *>.alias(alias: SFunction<T, *>): String {
    return SqlAlias(this).alias(alias)
}

infix fun <T> KMutableProperty1<T, *>.`as`(alias: String): String {
    return alias(alias)
}

infix fun <T> KMutableProperty1<T, *>.`as`(alias: KMutableProperty1<T, *>): String {
    return alias(alias)
}

infix fun <T> SFunction<T, *>.`as`(alias: String): String {
    return alias(alias)
}

infix fun <T> SFunction<T, *>.`as`(alias: SFunction<T, *>): String {
    return alias(alias)
}
