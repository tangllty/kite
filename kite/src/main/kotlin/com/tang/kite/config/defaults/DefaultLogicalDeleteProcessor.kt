package com.tang.kite.config.defaults

import com.tang.kite.config.logical.LogicalDeleteProcessor
import com.tang.kite.config.logical.LogicalDeleteValue
import java.lang.reflect.Field

/**
 * Default implementation of LogicalDeleteProcessor
 *
 * @author Tang
 */
object DefaultLogicalDeleteProcessor : LogicalDeleteProcessor {

    override fun process(field: Field): LogicalDeleteValue {
        return when (field.type) {
            Boolean::class.java, Boolean::class.javaObjectType -> LogicalDeleteValue(normalValue = false, deletedValue = true)
            Byte::class.java, Byte::class.javaObjectType -> LogicalDeleteValue(0.toByte(), 1.toByte())
            Short::class.java, Short::class.javaObjectType -> LogicalDeleteValue(0.toShort(), 1.toShort())
            Int::class.java, Int::class.javaObjectType -> LogicalDeleteValue(0, 1)
            Long::class.java, Long::class.javaObjectType -> LogicalDeleteValue(0L, 1L)
            Char::class.java, Char::class.javaObjectType -> LogicalDeleteValue('0', '1')
            String::class.java -> LogicalDeleteValue("0", "1")
            else -> throw IllegalArgumentException("Unsupported logical delete type: ${field.type}")
        }
    }

}
