package com.tang.kite.handler.fill

import com.tang.kite.sql.enumeration.ComparisonOperator
import java.lang.reflect.Field

/**
 * @author Tang
 */
interface FillHandler {

    fun fillValue(annotation: Annotation, field: Field, entity: Any): Any?

}
