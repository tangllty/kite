package com.tang.kite.result

import java.lang.reflect.Field

/**
 * @author Tang
 */
class DefaultResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        field.set(instance, value)
    }

}
