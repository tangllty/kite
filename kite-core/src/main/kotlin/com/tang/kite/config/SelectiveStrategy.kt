package com.tang.kite.config

/**
 * Selective strategy interface
 *
 * @author Tang
 */
interface SelectiveStrategy {

    fun isSelective(any: Any?): Boolean

}
