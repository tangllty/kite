package com.tang.kite.config

/**
 * Selective strategy interface
 *
 * @author Tang
 */
interface SelectiveStrategy {

    /**
     * Check if the object is selective
     *
     * @param any object to check
     * @return true if selective, false otherwise
     */
    fun isSelective(any: Any?): Boolean

}
