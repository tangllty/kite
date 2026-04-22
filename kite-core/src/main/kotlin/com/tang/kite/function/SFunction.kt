package com.tang.kite.function

import java.io.Serializable
import java.util.function.Function

/**
 * @author Tang
 */
@FunctionalInterface
interface SFunction<T, R> : Function<T, R>, Serializable
