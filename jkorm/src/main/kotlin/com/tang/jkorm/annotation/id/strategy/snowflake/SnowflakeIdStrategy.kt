package com.tang.jkorm.annotation.id.strategy.snowflake

import com.tang.jkorm.annotation.id.strategy.IdStrategy
import com.tang.jkorm.utils.id.Ids
import java.lang.reflect.Field

/**
 * @author Tang
 */
class SnowflakeIdStrategy : IdStrategy {

    override fun getId(idField: Field): Any {
        return Ids.snowflake().let { if (idField.type == String::class.java) it.toString() else it }
    }

}
