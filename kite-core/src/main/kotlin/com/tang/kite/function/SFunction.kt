package com.tang.kite.function

import java.io.Serializable
import java.util.function.Function

/**
 * Serializable function interface used for type-safe lambda expressions in query building.
 * Extends [Function] and [Serializable] to enable serialization of lambda references
 * for extracting property names at compile time.
 *
 * @param T the type of the input to the function
 * @param R the type of the result of the function
 * @author Tang
 */
@FunctionalInterface
interface SFunction<T, R> : Function<T, R>, Serializable
