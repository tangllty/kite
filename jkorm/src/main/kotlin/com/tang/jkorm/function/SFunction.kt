package com.tang.jkorm.function

import java.io.Serializable
import java.util.function.Function

/**
 * @author Tang
 */
@FunctionalInterface
interface SFunction<T, R> : Function<T, R>, Serializable
