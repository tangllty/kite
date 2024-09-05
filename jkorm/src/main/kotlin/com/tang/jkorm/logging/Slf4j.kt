package com.tang.jkorm.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Tang
 */
inline val <reified T> T.getLogger: Logger get() = LoggerFactory.getLogger(T::class.java)

inline val <reified T> T.LOGGER get() = getLogger
