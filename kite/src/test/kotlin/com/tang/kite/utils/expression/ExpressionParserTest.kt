package com.tang.kite.utils.expression

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Tang
 */
class ExpressionParserTest {

    val context = mapOf(
        "age" to 25,
        "name" to "Alice",
        "hobbies" to listOf("reading", "swimming", "coding"),
        "user" to mapOf(
            "age" to 30,
            "profile" to mapOf(
                "nickname" to "Bob",
                "tags" to listOf("vip", "admin")
            ),
            "hobbies" to listOf("basketball", "music")
        ),
        "arr" to arrayOf(1, 2, 3, 4)
    )

    @Test
    fun testExpressionParsing() {
        val expressions = listOf(
            Pair("age > 20", true),
            Pair("name != null", true),
            Pair("name == \"Alice\" && age >= 25", true),
            Pair("hobbies.contains(\"swimming\")", true),
            Pair("name.startsWith(\"Ali\")", true),
            Pair("age + 5 < 30", false),
            Pair("name + 'Smith'", "AliceSmith"),
            Pair("1 * (1 + 2 + (2 * 2))", 7),
            Pair("user.age > 20", true),
            Pair("user.profile.nickname == \"Bob\"", true),
            Pair("user.profile.tags.contains(\"vip\")", true),
            Pair("user.hobbies.size() == 2", true),
            Pair("user.profile.tags.size() == 2", true),
            Pair("arr.length() == 4", true),
            Pair("user.profile.nickname.startsWith(\"B\")", true),
            Pair("user.profile.nickname.length == 3", true),
            Pair("age > 20 && (hobbies.contains('reading') || hobbies.contains('coding'))", true),
            Pair("user == 'null'", false),
            Pair("user != null", true),
            Pair("user == null == false", true),
            Pair("true == true", true),
            Pair("false == false", true),
            Pair("true != false", true),
            Pair("null != null", false),
            Pair("null == null", true),
            Pair("arr[0]", 1),
            Pair("arr[0] == 1", true),
            Pair("user.profile", mapOf("nickname" to "Bob", "tags" to listOf("vip", "admin"))),
            Pair("user.profile.tags[1] == 'admin'", true),
            Pair("user['profile']['nickname']", "Bob"),
            Pair("3 % 2", 1),
            Pair("3 ^ 2", 9)
        )
        expressions.forEach {
            val value = ExpressionParser.evaluate(it.first, context)
            assertEquals(value, it.second, "Expected '${it.first}' to evaluate to ${it.second}, but got $value")
        }
    }

}
