package com.tang.kite.exception

import java.lang.reflect.Field
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class UnsupportedTypeException : RuntimeException {

    constructor(message: String?) : super(message)

    constructor() : this("Unsupported type")

    constructor(clazz: Class<*>) : this("Unsupported type: ${clazz.name}")

    constructor(clazz: KClass<*>) : this("Unsupported type: ${clazz.qualifiedName ?: clazz.simpleName}")

    constructor(clazz: Class<*>, field: Field) : this("Unsupported type: ${clazz.name} for field: ${field.name}")

    constructor(clazz: KClass<*>, field: Field) : this("Unsupported type: ${clazz.qualifiedName ?: clazz.simpleName} for field: ${field.name}")

}
