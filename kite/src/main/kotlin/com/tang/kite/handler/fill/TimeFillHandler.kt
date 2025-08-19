package com.tang.kite.handler.fill

import com.tang.kite.exception.UnsupportedTypeException
import java.lang.reflect.Field
import java.sql.Date as SqlDate
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.Instant as KotlinInstant

/**
 * @author Tang
 */
class TimeFillHandler : FillHandler {

    @OptIn(ExperimentalTime::class)
    override fun fillValue(annotation: Annotation, field: Field, entity: Any): Any? {
        return when (field.type) {
            Time::class.java -> Time(System.currentTimeMillis())
            SqlDate::class.java -> SqlDate(System.currentTimeMillis())
            Timestamp::class.java -> Timestamp(System.currentTimeMillis())
            Date::class.java -> Date()
            Calendar::class.java -> Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
            Instant::class.java -> Instant.now()
            KotlinInstant::class.java -> Clock.System.now().toJavaInstant()
            LocalTime::class.java -> LocalTime.now()
            LocalDate::class.java -> LocalDate.now()
            LocalDateTime::class.java -> LocalDateTime.now()
            OffsetTime::class.java -> OffsetTime.now()
            OffsetDateTime::class.java -> OffsetDateTime.now()
            ZonedDateTime::class.java -> ZonedDateTime.now()
            else -> throw UnsupportedTypeException(entity::class, field)
        }
    }

}
