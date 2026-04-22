package com.tang.kite.config.defaults

import com.tang.kite.config.SelectiveStrategy

/**
 * Default selective strategy
 *
 * @author Tang
 */
object DefaultSelectiveStrategy : SelectiveStrategy {

    override fun isSelective(any: Any?): Boolean {
        return any != null
    }

}
