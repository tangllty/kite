package com.tang.kite.handler.field

import java.lang.reflect.Field

/**
 * Field handler for handling field values.
 *
 * @author Tang
 */
interface FieldHandler {

    /**
     * Handle the value of a field.
     *
     * @param annotation The annotation for the field.
     * @param field      The field to handle.
     * @param entity     The entity containing the field.
     * @return The value of the field.
     */
    fun handleValue(annotation: Annotation, field: Field, entity: Any): Any?

}
