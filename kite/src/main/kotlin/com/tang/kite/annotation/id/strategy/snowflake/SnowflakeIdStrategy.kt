package com.tang.kite.annotation.id.strategy.snowflake

import com.tang.kite.annotation.id.strategy.IdStrategy
import com.tang.kite.utils.id.Ids
import java.lang.reflect.Field

/**
 * @author Tang
 */
class SnowflakeIdStrategy : IdStrategy {

    override fun getId(idField: Field): Any {
        return Ids.snowflake().let { if (idField.type == String::class.java) it.toString() else it }
    }

}
