package com.tang.kite.result

import java.lang.reflect.Field

/**
 * Interface for handling results in a generic way.
 *
 * @author Tang
 */
interface ResultHandler {

    /**
     * Sets the value of the specified field in the given instance to null if the value is null.
     * If the value is not null, it calls the [setValue] method to set the value.
     *
     * @param field The field to set.
     * @param instance The instance whose field is to be set.
     */
    fun <T> setNullValue(field: Field, instance: T) {
        field.set(instance, null)
    }

    /**
     * Sets the value of the specified field in the given instance.
     *
     * @param field The field to set.
     * @param instance The instance whose field is to be set.
     * @param value The value to set in the field.
     */
    fun <T> setValue(field: Field, instance: T, value: Any)

}
