package com.tang.kite.result

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class EnumResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val enumClass = field.type as Class<*>
        val enumConstants = enumClass.enumConstants
        val enumValue = when (value) {
            is Enum<*> -> value
            is String -> {
                enumConstants.firstOrNull { (it as Enum<*>).name == value }
                    ?: throw IllegalArgumentException("No enum constant ${enumClass.name}.$value")
            }
            is Number -> {
                enumConstants.firstOrNull { (it as Enum<*>).ordinal == value.toInt() }
                    ?: throw IllegalArgumentException("No enum constant ${enumClass.name} with ordinal $value")
            }
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, enumValue)
    }

}
