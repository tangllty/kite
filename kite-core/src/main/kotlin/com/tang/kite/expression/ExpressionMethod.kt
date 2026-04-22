package com.tang.kite.expression

/**
 * @author Tang
 */
interface ExpressionMethod {

    /**
     * Call the method with the given value and arguments.
     *
     * @param target The target value for the method call.
     * @param args The arguments to pass to the method.
     * @return The result of the method call.
     */
    fun call(target: Any?, args: List<Any?>): Any?

}
