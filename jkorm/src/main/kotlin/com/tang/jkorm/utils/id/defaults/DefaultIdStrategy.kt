package com.tang.jkorm.utils.id.defaults

import com.tang.jkorm.utils.id.IdStrategy
import com.tang.jkorm.utils.id.Ids
import java.lang.reflect.Field

/**
 * @author Tang
 */
class DefaultIdStrategy : IdStrategy {

    override fun getId(idField: Field): Any {
        return Ids.snowflake().let { if (idField.type == String::class.java) it.toString() else it }
    }

}
