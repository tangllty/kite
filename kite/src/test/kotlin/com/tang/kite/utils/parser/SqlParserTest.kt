package com.tang.kite.utils.parser

import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class SqlParserTest {

    @Test
    fun noCondition() {
        val sql = "select * from user"
        val params = emptyMap<String, Any?>()
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user")
        assert(parsed.parameters.isEmpty())
    }

    @Test
    fun withCondition() {
        val sql = "select * from user where age > #{age} and name = #{name}"
        val params = mapOf("age" to 18, "name" to "Tang")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun withNestedProperties() {
        val sql = "select * from user where age > #{user.age} and name = #{user.name}"
        val params = mapOf("user" to mapOf("age" to 18, "name" to "Tang"))
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun withBlankLinesAndSpaces() {
        val sql = """
            select *  from user
            where age>#{age} and name = #{name}
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to "Tang")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun withMultipleConditions() {
        val sql = """
            select * from user
            where age > #{age}
                and (
                    name = #{name}
                    or email = #{email}
                )
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to "Tang", "email" to "tang@gmail.com")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and (name = ? or email = ?)")
        assert(parsed.parameters == listOf(18, "Tang", "tang@gmail.com"))
    }

    @Test
    fun withSpecialCharacters() {
        val sql = "select * from user where name = #{name} and email like #{email}"
        val params = mapOf("name" to "Tang", "email" to "%@gmail.com")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where name = ? and email like ?")
        assert(parsed.parameters == listOf("Tang", "%@gmail.com"))
    }

    @Test
    fun withEmptyParams() {
        val sql = "select * from user where age > #{age} and name = #{name}"
        val params = emptyMap<String, Any?>()
        try {
            SqlParser.parse(sql, params)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("parameter 'age' not found") == true)
        }
    }

    @Test
    fun withInvalidSyntax() {
        val sql = "select * from user where age > #{age and name = #{name}"
        val params = mapOf("age" to 18, "name" to "Tang")
        try {
            SqlParser.parse(sql, params)
            assert(false)
        } catch (e: Exception) {
            assert(e.message?.contains("SQL syntax error: unmatched #{} placeholder") == true)
        }
    }

    @Test
    fun withUnmatchedParentheses() {
        val sql = "select * from user where (age > #{age} and name = #{name}"
        val params = mapOf("age" to 18, "name" to "Tang")
        try {
            SqlParser.parse(sql, params)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("SQL syntax error: unmatched '(' or ')' in SQL.") == true)
        }
    }

    @Test
    fun withIfCondition() {
        val sql = """
            select * from user
            where age > #{age}
            if (name != null) {
                and name = #{name}
            }
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to "Tang")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun withIfConditionFalse() {
        val sql = """
            select * from user
            where age > #{age}
            if (name != null) {
                and name = #{name}
            }
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to null)
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ?")
        assert(parsed.parameters == listOf(18))
    }

    @Test
    fun withIfMultipleConditions() {
        val sql = """
            select * from user
            where age > #{age}
            if (name != null) {
                and name = #{name}
            }
            if (email != null) {
                and email = #{email}
            }
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to "Tang", "email" to "tang@gmail.com")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ? and email = ?")
        assert(parsed.parameters == listOf(18, "Tang", "tang@gmail.com"))
    }

    @Test
    fun autoAppendWhereConditionsWithParams() {
        val sql = """
            select * from user
            if (age != null) {
                and age > #{age}
            }
            if (name != null) {
                and name = #{name}
            }
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to "Tang")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun autoAppendWhereConditionsWithNoParams() {
        val sql = """
            select * from user
            if (age != null) {
                and age > #{age}
            }
            if (name != null) {
                and name = #{name}
            }
        """.trimIndent()
        val params = mapOf("age" to null, "name" to null)
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user")
        assert(parsed.parameters.isEmpty())
    }

    @Test
    fun autoAppendWhereWithGroupBy() {
        val sql = """
            select count(1) from user
            if (groupBy != null) {
                group by #{groupBy}
            }
        """.trimIndent()
        val params = mapOf("groupBy" to "age")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select count(1) from user group by ?")
        assert(parsed.parameters == listOf("age"))
    }

    @Test
    fun autoAppendWhereWithOrderBy() {
        val sql = """
            select * from user
            if (orderBy != null) {
                order by #{orderBy}
            }
        """.trimIndent()
        val params = mapOf("orderBy" to "age desc")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user order by ?")
        assert(parsed.parameters == listOf("age desc"))
    }

    @Test
    fun autoAppendWhereWithLimit() {
        val sql = """
            select * from user
            if (limit != null) {
                limit #{limit}
            }
        """.trimIndent()
        val params = mapOf("limit" to 10)
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user limit ?")
        assert(parsed.parameters == listOf(10))
    }

    @Test
    fun withIfNestedProperties() {
        val sql = """
            select * from user
            if (user != null && user.age != null) {
                and age > #{user.age}
            }
            if (user != null && user.name != null) {
                and name = #{user.name}
            }
        """.trimIndent()
        val params = mapOf("user" to mapOf("age" to 18, "name" to "Tang"))
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun withIfNestedPropertiesWithNull() {
        val sql = """
            select * from user
            if (user != null && user.age != null) {
                and age > #{user.age}
            }
            if (user != null && user.name != null) {
                and name = #{user.name}
            }
        """.trimIndent()
        val params = mapOf("user" to mapOf("age" to null, "name" to "Tang"))
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where name = ?")
        assert(parsed.parameters == listOf("Tang"))
    }

    @Test
    fun withIfConditionWithMultipleNestedProperties() {
        val sql = """
            select * from user
            if (user != null) {
                and age > #{user.age}
                and name = #{user.name}
            }
        """.trimIndent()
        val params = mapOf("user" to mapOf("age" to 18, "name" to "Tang"))
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun nestedIfConditions() {
        val sql = """
            select * from user
            if (user != null) {
                and age > #{user.age}
                if (user.name != null) {
                    and name = #{user.name}
                }
            }
        """.trimIndent()
        val params = mapOf("user" to mapOf("age" to 18, "name" to "Tang"))
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name = ?")
        assert(parsed.parameters == listOf(18, "Tang"))
    }

    @Test
    fun withElseCondition() {
        val sql = """
            select * from user
            if (age != null) {
                and age > #{age}
            } else {
                and age is null
            }
        """.trimIndent()
        val params = mapOf("age" to 18)
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ?")
        assert(parsed.parameters == listOf(18))

        val params2 = mapOf("age" to null)
        val parsed2 = SqlParser.parse(sql, params2)
        assert(parsed2.sql == "select * from user where age is null")
        assert(parsed2.parameters.isEmpty())
    }

    @Test
    fun withElseIfCondition() {
        val sql = """
            select * from user
            if (age != null) {
                and age > #{age}
            } else if (name != null) {
                and name = #{name}
            }
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to null)
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ?")
        assert(parsed.parameters == listOf(18))

        val params2 = mapOf("age" to null, "name" to "Tang")
        val parsed2 = SqlParser.parse(sql, params2)
        assert(parsed2.sql == "select * from user where name = ?")
        assert(parsed2.parameters == listOf("Tang"))
    }

    @Test
    fun withElseIfElseCondition() {
        val sql = """
            select * from user
            if (age != null) {
                and age > #{age}
            } else if (name != null) {
                and name = #{name}
            } else {
                and email is null
            }
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to null)
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ?")
        assert(parsed.parameters == listOf(18))

        val params2 = mapOf("age" to null, "name" to "Tang")
        val parsed2 = SqlParser.parse(sql, params2)
        assert(parsed2.sql == "select * from user where name = ?")
        assert(parsed2.parameters == listOf("Tang"))

        val params3 = mapOf("age" to null, "name" to null)
        val parsed3 = SqlParser.parse(sql, params3)
        assert(parsed3.sql == "select * from user where email is null")
        assert(parsed3.parameters.isEmpty())
    }

    @Test
    fun sqlFunctions() {
        val sql = """
            select * from user
            where age > #{age}
            and name like concat('%', #{name}, '%')
            and date_format(created_at, '%Y-%m-%d') = #{date}
        """.trimIndent()
        val params = mapOf("age" to 18, "name" to "Tang", "date" to "2023-10-01")
        val parsed = SqlParser.parse(sql, params)
        assert(parsed.sql == "select * from user where age > ? and name like concat('%', ?, '%') and date_format(created_at, '%Y-%m-%d') = ?")
        assert(parsed.parameters == listOf(18, "Tang", "2023-10-01"))
    }

}
