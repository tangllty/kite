package com.tang.jkorm.config.defaults

import com.tang.jkorm.config.SelectiveStrategy

/**
 * Default selective strategy
 *
 * @author Tang
 */
object DefaultSelectiveStrategy: SelectiveStrategy {

    override fun isSelective(any: Any?): Boolean {
        return when (any) {
            null -> false
            is String -> any.isNotBlank()
            is Int -> any != 0
            is Long -> any != 0L
            is Iterable<*> -> any.iterator().hasNext()
            else -> true
        }
    }

}
