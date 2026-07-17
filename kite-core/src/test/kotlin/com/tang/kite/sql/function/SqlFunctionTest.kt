package com.tang.kite.sql.function

import com.tang.kite.sql.Column
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Tang
 */
class SqlFunctionTest {

    data class Account(
        val id: Long = 0,
        val name: String = "",
        val email: String = "",
        val age: Int = 0,
        val score: Double = 0.0,
        val salary: BigDecimal = BigDecimal.ZERO,
        val status: Int = 0,
        val deptId: Long = 0,
        val createdAt: String = "",
        val amount: Double = 0.0
    )

    @Test
    fun testAvg() {
        assertEquals("avg(score)", SqlFunction.avg("score").render())
        assertEquals("avg(score)", SqlFunction.avg(Account::score).render())
    }

    @Test
    fun testAnyValue() {
        assertEquals("any_value(score)", SqlFunction.anyValue("score").render())
        assertEquals("any_value(score)", SqlFunction.anyValue(Account::score).render())
    }

    @Test
    fun testCount() {
        assertEquals("count(*)", SqlFunction.count().render())
        assertEquals("count(id)", SqlFunction.count("id").render())
        assertEquals("count(id)", SqlFunction.count(Account::id).render())
    }

    @Test
    fun testMax() {
        assertEquals("max(score)", SqlFunction.max("score").render())
        assertEquals("max(score)", SqlFunction.max(Account::score).render())
    }

    @Test
    fun testMin() {
        assertEquals("min(salary)", SqlFunction.min("salary").render())
        assertEquals("min(salary)", SqlFunction.min(Account::salary).render())
    }

    @Test
    fun testSum() {
        assertEquals("sum(amount)", SqlFunction.sum("amount").render())
        assertEquals("sum(amount)", SqlFunction.sum(Account::amount).render())
    }

    @Test
    fun testDistinct() {
        assertEquals("count(distinct id)", SqlFunction.count("id").distinct().render())
        assertEquals("count(distinct id)", SqlFunction.count(Account::id).distinct().render())
    }

    @Test
    fun testAll() {
        assertEquals("count(all id)", SqlFunction.count("id").all().render())
        assertEquals("count(all id)", SqlFunction.count(Account::id).all().render())
    }

    @Test
    fun testLength() {
        assertEquals("length(name)", SqlFunction.length("name").render())
        assertEquals("length(name)", SqlFunction.length(Account::name).render())
    }

    @Test
    fun testLower() {
        assertEquals("lower(name)", SqlFunction.lower("name").render())
        assertEquals("lower(name)", SqlFunction.lower(Account::name).render())
    }

    @Test
    fun testUpper() {
        assertEquals("upper(email)", SqlFunction.upper("email").render())
        assertEquals("upper(email)", SqlFunction.upper(Account::email).render())
    }

    @Test
    fun testTrims() {
        assertEquals("trim(name)", SqlFunction.trim("name").render())
        assertEquals("trim(name)", SqlFunction.trim(Account::name).render())
        assertEquals("trim(both '_' from name)", SqlFunction.trimBoth("_", "name").render())
        assertEquals("trim(both '_' from name)", SqlFunction.trimBoth("_", Account::name).render())
        assertEquals("trim(leading '0' from name)", SqlFunction.trimLeading("0", "name").render())
        assertEquals("trim(leading '0' from name)", SqlFunction.trimLeading("0", Account::name).render())
        assertEquals("trim(trailing ' ' from name)", SqlFunction.trimTrailing(" ", "name").render())
        assertEquals("trim(trailing ' ' from name)", SqlFunction.trimTrailing(" ", Account::name).render())
    }

    @Test
    fun testConcat() {
        assertEquals("concat(id, name)", SqlFunction.concat("id", "name").render())
        assertEquals("concat(id, name)", SqlFunction.concat(Account::id, Account::name).render())
    }

    @Test
    fun testSubstring() {
        assertEquals("substring(name, 1, 10)", SqlFunction.substring("name", 1, 10).render())
        assertEquals("substring(name, 5)", SqlFunction.substring(Account::name, 5).render())
    }

    @Test
    fun testReplace() {
        assertEquals("replace(name, 'old', 'new')", SqlFunction.replace("name", "old", "new").render())
        assertEquals("replace(name, 'old', 'new')", SqlFunction.replace(Account::name, "old", "new").render())
    }

    @Test
    fun testPosition() {
        assertEquals("position('@' in email)", SqlFunction.position("@", "email").render())
        assertEquals("position('@' in email)", SqlFunction.position("@", Account::email).render())
    }

    @Test
    fun testAbs() {
        assertEquals("abs(score)", SqlFunction.abs("score").render())
        assertEquals("abs(score)", SqlFunction.abs(Account::score).render())
    }

    @Test
    fun testRound() {
        assertEquals("round(score)", SqlFunction.round("score").render())
        assertEquals("round(score, 2)", SqlFunction.round("score", 2).render())
        assertEquals("round(score)", SqlFunction.round(Account::score).render())
        assertEquals("round(score, 2)", SqlFunction.round(Account::score, 2).render())
    }

    @Test
    fun testCeil() {
        assertEquals("ceil(score)", SqlFunction.ceil("score").render())
        assertEquals("ceil(score)", SqlFunction.ceil(Account::score).render())
    }

    @Test
    fun testCeiling() {
        assertEquals("ceiling(score)", SqlFunction.ceiling("score").render())
        assertEquals("ceiling(score)", SqlFunction.ceiling(Account::score).render())
    }

    @Test
    fun testFloor() {
        assertEquals("floor(score)", SqlFunction.floor("score").render())
        assertEquals("floor(score)", SqlFunction.floor(Account::score).render())
    }

    @Test
    fun testMod() {
        assertEquals("mod(score, 10)", SqlFunction.mod("score", 10).render())
        assertEquals("mod(score, 10)", SqlFunction.mod(Account::score, 10).render())
    }

    @Test
    fun testPower() {
        assertEquals("power(score, 2)", SqlFunction.power("score", 2).render())
        assertEquals("power(score, 2)", SqlFunction.power(Account::score, 2).render())
    }

    @Test
    fun testSqrt() {
        assertEquals("sqrt(score)", SqlFunction.sqrt("score").render())
        assertEquals("sqrt(score)", SqlFunction.sqrt(Account::score).render())
    }

    @Test
    fun testExp() {
        assertEquals("exp(score)", SqlFunction.exp("score").render())
        assertEquals("exp(score)", SqlFunction.exp(Account::score).render())
    }

    @Test
    fun testLog() {
        assertEquals("log(score)", SqlFunction.log("score").render())
        assertEquals("log(score)", SqlFunction.log(Account::score).render())
    }

    @Test
    fun testLog10() {
        assertEquals("log10(score)", SqlFunction.log10("score").render())
        assertEquals("log10(score)", SqlFunction.log10(Account::score).render())
    }

    @Test
    fun testSin() {
        assertEquals("sin(score)", SqlFunction.sin("score").render())
        assertEquals("sin(score)", SqlFunction.sin(Account::score).render())
    }

    @Test
    fun testCos() {
        assertEquals("cos(score)", SqlFunction.cos("score").render())
        assertEquals("cos(score)", SqlFunction.cos(Account::score).render())
    }

    @Test
    fun testTan() {
        assertEquals("tan(score)", SqlFunction.tan("score").render())
        assertEquals("tan(score)", SqlFunction.tan(Account::score).render())
    }

    @Test
    fun testAsin() {
        assertEquals("asin(score)", SqlFunction.asin("score").render())
        assertEquals("asin(score)", SqlFunction.asin(Account::score).render())
    }

    @Test
    fun testAcos() {
        assertEquals("acos(score)", SqlFunction.acos("score").render())
        assertEquals("acos(score)", SqlFunction.acos(Account::score).render())
    }

    @Test
    fun testAtan() {
        assertEquals("atan(score)", SqlFunction.atan("score").render())
        assertEquals("atan(score)", SqlFunction.atan(Account::score).render())
    }

    @Test
    fun testSign() {
        assertEquals("sign(score)", SqlFunction.sign("score").render())
        assertEquals("sign(score)", SqlFunction.sign(Account::score).render())
    }

    @Test
    fun testPi() {
        assertEquals("pi()", SqlFunction.pi().render())
    }

    @Test
    fun testCurrentDate() {
        assertEquals("current_date", SqlFunction.currentDate().render())
    }

    @Test
    fun testCurrentTime() {
        assertEquals("current_time", SqlFunction.currentTime().render())
    }

    @Test
    fun testCurrentTimestamp() {
        assertEquals("current_timestamp", SqlFunction.currentTimestamp().render())
    }

    @Test
    fun testDate() {
        assertEquals("extract(date from created_at)", SqlFunction.date("created_at").render())
        assertEquals("extract(date from created_at)", SqlFunction.date(Account::createdAt).render())
    }

    @Test
    fun testTime() {
        assertEquals("extract(time from created_at)", SqlFunction.time("created_at").render())
        assertEquals("extract(time from created_at)", SqlFunction.time(Account::createdAt).render())
    }

    @Test
    fun testYear() {
        assertEquals("extract(year from created_at)", SqlFunction.year("created_at").render())
        assertEquals("extract(year from created_at)", SqlFunction.year(Account::createdAt).render())
    }

    @Test
    fun testMonth() {
        assertEquals("extract(month from created_at)", SqlFunction.month("created_at").render())
        assertEquals("extract(month from created_at)", SqlFunction.month(Account::createdAt).render())
    }

    @Test
    fun testDay() {
        assertEquals("extract(day from created_at)", SqlFunction.day("created_at").render())
        assertEquals("extract(day from created_at)", SqlFunction.day(Account::createdAt).render())
    }

    @Test
    fun testHour() {
        assertEquals("extract(hour from created_at)", SqlFunction.hour("created_at").render())
        assertEquals("extract(hour from created_at)", SqlFunction.hour(Account::createdAt).render())
    }

    @Test
    fun testMinute() {
        assertEquals("extract(minute from created_at)", SqlFunction.minute("created_at").render())
        assertEquals("extract(minute from created_at)", SqlFunction.minute(Account::createdAt).render())
    }

    @Test
    fun testSecond() {
        assertEquals("extract(second from created_at)", SqlFunction.second("created_at").render())
        assertEquals("extract(second from created_at)", SqlFunction.second(Account::createdAt).render())
    }

    @Test
    fun testCoalesce() {
        assertEquals("coalesce(name, 'unknown')", SqlFunction.coalesce("name", "'unknown'").render())
        assertEquals("coalesce(name, 'unknown')", SqlFunction.coalesce(Account::name, "unknown").render())
        assertEquals("coalesce(name, email)", SqlFunction.coalesce("name", "email").render())
        assertEquals("coalesce(name, email)", SqlFunction.coalesce(Account::name, Account::email).render())
    }

    @Test
    fun testNullif() {
        assertEquals("nullif(name, '')", SqlFunction.nullif("name", "").render())
        assertEquals("nullif(name, '')", SqlFunction.nullif(Account::name, "").render())
        assertEquals("nullif(status, 0)", SqlFunction.nullif("status", 0).render())
    }

    @Test
    fun testCase() {
        assertEquals("case when 'a' then 'b' end", SqlFunction.case().`when`("a", "b").render())
        assertEquals("case status when 1 then 'active' when 0 then 'inactive' else 'unknown' end",
            SqlFunction.case("status").`when`(1, "active").`when`(0, "inactive").`else`("unknown").render())
        assertEquals("case status when 1 then 'active' when 0 then 'inactive' else 'unknown' end",
            SqlFunction.case(Account::status).`when`(1, "active").`when`(0, "inactive").`else`("unknown").render())
    }

}
