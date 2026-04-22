package com.tang.kite.handler.fill

import java.lang.reflect.Field

/**
 * @author Tang
 */
interface FillHandler {

    fun fillValue(annotation: Annotation, field: Field, entity: Any): Any?

}
