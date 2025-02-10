package com.tang.kite.utils.id

/**
 * @author Tang
 */
object Ids {

    private val snowflake = Snowflake(1, 1)

    fun snowflake(): Long {
        return snowflake.nextId()
    }

}
