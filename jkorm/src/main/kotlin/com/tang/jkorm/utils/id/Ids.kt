package com.tang.jkorm.utils.id

/**
 * @author Tang
 */
object Ids {

    private val snowflake = Snowflake(1, 1)

    fun snowflake(): Long {
        return snowflake.nextId()
    }

}
