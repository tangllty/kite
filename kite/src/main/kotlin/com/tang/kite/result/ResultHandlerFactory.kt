package com.tang.kite.result

import com.tang.kite.result.math.BigDecimalResultHandler
import com.tang.kite.result.math.BigIntegerResultHandler
import com.tang.kite.result.primitive.BooleanResultHandler
import com.tang.kite.result.primitive.ByteResultHandler
import com.tang.kite.result.primitive.CharResultHandler
import com.tang.kite.result.primitive.DoubleResultHandler
import com.tang.kite.result.primitive.FloatResultHandler
import com.tang.kite.result.primitive.IntResultHandler
import com.tang.kite.result.primitive.LongResultHandler
import com.tang.kite.result.primitive.ShortResultHandler
import com.tang.kite.result.primitive.StringResultHandler
import com.tang.kite.result.time.CalendarResultHandler
import com.tang.kite.result.time.DateResultHandler
import com.tang.kite.result.time.InstantResultHandler
import com.tang.kite.result.time.LocalDateResultHandler
import com.tang.kite.result.time.LocalDateTimeResultHandler
import com.tang.kite.result.time.LocalTimeResultHandler
import com.tang.kite.result.time.SqlDateResultHandler
import com.tang.kite.result.time.TimeResultHandler
import com.tang.kite.result.time.TimestampResultHandler
import java.lang.reflect.Field
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Date as SqlDate
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import kotlin.Double
import kotlin.Long

/**
 * @author Tang
 */
class ResultHandlerFactory {

    fun newResultHandler(field: Field): ResultHandler {
        return when (field.type) {
            // Primitive types
            Char::class.java -> CharResultHandler()
            Short::class.java -> ShortResultHandler()
            Byte::class.java -> ByteResultHandler()
            Int::class.java, Int::class.javaObjectType, Integer::class.java -> IntResultHandler()
            Long::class.java, Long::class.javaObjectType -> LongResultHandler()
            Double::class.java -> DoubleResultHandler()
            Float::class.java -> FloatResultHandler()
            Boolean::class.java -> BooleanResultHandler()
            String::class.java -> StringResultHandler()
            // Date and time types
            SqlDate::class.java -> SqlDateResultHandler()
            Time::class.java -> TimeResultHandler()
            Timestamp::class.java -> TimestampResultHandler()
            Calendar::class.java -> CalendarResultHandler()
            Date::class.java -> DateResultHandler()
            Instant::class.java -> InstantResultHandler()
            LocalDate::class.java -> LocalDateResultHandler()
            LocalTime::class.java -> LocalTimeResultHandler()
            LocalDateTime::class.java -> LocalDateTimeResultHandler()
            // Math types
            BigDecimal::class.java -> BigDecimalResultHandler()
            BigInteger::class.java -> BigIntegerResultHandler()
            // Enum types
            Enum::class.java -> EnumResultHandler()
            else -> DefaultResultHandler()
        }
    }

}
