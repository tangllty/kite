package com.tang.kite.utils

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class CaseFormatTest {

    @Test
    fun to() {
        val testCases = listOf(
            // Hyphen format tests
            Triple(CaseFormat.LOWER_HYPHEN, "hello-world", CaseFormat.LOWER_UNDERSCORE to "hello_world"),
            Triple(CaseFormat.LOWER_HYPHEN, "hello-world", CaseFormat.LOWER_CAMEL to "helloWorld"),
            Triple(CaseFormat.LOWER_HYPHEN, "hello-world", CaseFormat.UPPER_CAMEL to "HelloWorld"),
            Triple(CaseFormat.LOWER_HYPHEN, "hello-world", CaseFormat.UPPER_UNDERSCORE to "HELLO_WORLD"),

            // Lowercase underscore format tests
            Triple(CaseFormat.LOWER_UNDERSCORE, "user_name", CaseFormat.LOWER_HYPHEN to "user-name"),
            Triple(CaseFormat.LOWER_UNDERSCORE, "user_name", CaseFormat.LOWER_CAMEL to "userName"),
            Triple(CaseFormat.LOWER_UNDERSCORE, "user_name", CaseFormat.UPPER_CAMEL to "UserName"),
            Triple(CaseFormat.LOWER_UNDERSCORE, "user_name", CaseFormat.UPPER_UNDERSCORE to "USER_NAME"),

            // Lower camel case format tests
            Triple(CaseFormat.LOWER_CAMEL, "camelCaseTest", CaseFormat.LOWER_HYPHEN to "camel-case-test"),
            Triple(CaseFormat.LOWER_CAMEL, "camelCaseTest", CaseFormat.LOWER_UNDERSCORE to "camel_case_test"),
            Triple(CaseFormat.LOWER_CAMEL, "camelCaseTest", CaseFormat.UPPER_CAMEL to "CamelCaseTest"),
            Triple(CaseFormat.LOWER_CAMEL, "camelCaseTest", CaseFormat.UPPER_UNDERSCORE to "CAMEL_CASE_TEST"),

            // Upper camel case (PascalCase) format tests
            Triple(CaseFormat.UPPER_CAMEL, "PascalCase", CaseFormat.LOWER_HYPHEN to "pascal-case"),
            Triple(CaseFormat.UPPER_CAMEL, "PascalCase", CaseFormat.LOWER_UNDERSCORE to "pascal_case"),
            Triple(CaseFormat.UPPER_CAMEL, "PascalCase", CaseFormat.LOWER_CAMEL to "pascalCase"),
            Triple(CaseFormat.UPPER_CAMEL, "PascalCase", CaseFormat.UPPER_UNDERSCORE to "PASCAL_CASE"),

            // Uppercase underscore format tests
            Triple(CaseFormat.UPPER_UNDERSCORE, "MAX_VALUE", CaseFormat.LOWER_HYPHEN to "max-value"),
            Triple(CaseFormat.UPPER_UNDERSCORE, "MAX_VALUE", CaseFormat.LOWER_UNDERSCORE to "max_value"),
            Triple(CaseFormat.UPPER_UNDERSCORE, "MAX_VALUE", CaseFormat.LOWER_CAMEL to "maxValue"),
            Triple(CaseFormat.UPPER_UNDERSCORE, "MAX_VALUE", CaseFormat.UPPER_CAMEL to "MaxValue"),

            // Edge case tests
            Triple(CaseFormat.LOWER_CAMEL, "user2Name", CaseFormat.LOWER_UNDERSCORE to "user2_name"),
            Triple(CaseFormat.LOWER_UNDERSCORE, "single", CaseFormat.UPPER_CAMEL to "Single"),
            Triple(CaseFormat.LOWER_HYPHEN, "", CaseFormat.UPPER_CAMEL to ""),
            Triple(CaseFormat.UPPER_CAMEL, "A", CaseFormat.LOWER_CAMEL to "a"),
            Triple(CaseFormat.UPPER_UNDERSCORE, "A_B_C", CaseFormat.LOWER_HYPHEN to "a-b-c")
        )

        var successCount = 0
        var failureCount = 0

        println("Starting CaseFormat conversion function tests...\n")

        for ((index, testCase) in testCases.withIndex()) {
            val (sourceFormat, input, targetAndExpected) = testCase
            val (targetFormat, expected) = targetAndExpected

            val result = sourceFormat.to(targetFormat, input)

            if (result == expected) {
                successCount++
                println("Test ${index + 1} passed: $input -> $result")
            } else {
                failureCount++
                println("Test ${index + 1} failed: $input | Expected: $expected | Actual: $result")
            }
        }

        println("\nTesting completed - Successes: $successCount, Failures: $failureCount")

        assertTrue { failureCount == 0 }

        if (failureCount == 0) {
            println("All tests passed!")
        }
    }

}
