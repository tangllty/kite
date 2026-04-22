package com.tang.kite.utils

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Blob
import java.sql.Clob
import java.sql.PreparedStatement
import java.sql.Date as SqlDate
import java.sql.Time
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration
import kotlin.time.toJavaInstant
import kotlin.time.Duration as KotlinDuration
import kotlin.time.Instant as KotlinInstant

/**
 * Statement utils
 *
 * @author Tang
 */
object Statements {

    fun setValues(statement: PreparedStatement, parameters: List<Any?>) {
        parameters.forEachIndexed { index, parameter ->
            setValue(statement, index + 1, parameter)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun setValue(statement: PreparedStatement, index: Int, parameter: Any?) {
        when (parameter) {
            null -> statement.setObject(index, null)
            is Int -> statement.setInt(index, parameter)
            is Long -> statement.setLong(index, parameter)
            is String -> statement.setString(index, parameter)
            is Boolean -> statement.setBoolean(index, parameter)
            is Byte -> statement.setByte(index, parameter)
            is Short -> statement.setShort(index, parameter)
            is Float -> statement.setFloat(index, parameter)
            is Double -> statement.setDouble(index, parameter)
            is BigDecimal -> statement.setBigDecimal(index, parameter)
            is BigInteger -> statement.setBigDecimal(index, parameter.toBigDecimal())
            is Time -> statement.setTime(index, parameter)
            is SqlDate -> statement.setDate(index, parameter)
            is Timestamp -> statement.setTimestamp(index, parameter)
            is Date -> statement.setTimestamp(index, Timestamp(parameter.time))
            is Calendar -> statement.setTimestamp(index, Timestamp(parameter.timeInMillis), parameter)
            is Instant -> statement.setTimestamp(index, Timestamp.from(parameter))
            is KotlinInstant -> statement.setTimestamp(index, Timestamp.from(parameter.toJavaInstant()))
            is Duration -> statement.setLong(index, parameter.toNanos())
            is KotlinDuration -> statement.setLong(index, parameter.toJavaDuration().toNanos())
            is LocalDate -> statement.setDate(index, SqlDate.valueOf(parameter))
            is LocalTime -> statement.setTime(index, Time.valueOf(parameter))
            is LocalDateTime -> statement.setTimestamp(index, Timestamp.valueOf(parameter))
            is OffsetDateTime -> statement.setTimestamp(index, Timestamp.valueOf(parameter.toLocalDateTime()))
            is OffsetTime -> statement.setTime(index, Time.valueOf(parameter.toLocalTime()))
            is ZonedDateTime -> statement.setTimestamp(index, Timestamp.valueOf(parameter.toLocalDateTime()))
            is Blob -> statement.setBlob(index, parameter)
            is Clob -> statement.setClob(index, parameter)
            is ByteArray -> statement.setBytes(index, parameter)
            is InputStream -> statement.setBinaryStream(index, parameter)
            is Reader -> statement.setClob(index, parameter)
            else -> statement.setObject(index, parameter)
        }
    }

}
