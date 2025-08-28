package com.tang.kite.utils

/**
 * Simple case format conversion utility
 *
 * @author Tang
 */
enum class CaseFormat {

    /**
     * Lower hyphen: lower-hyphen
     */
    LOWER_HYPHEN,

    /**
     * Lower underscore: lower_underscore
     */
    LOWER_UNDERSCORE,

    /**
     * Lower camel: lowerCamel
     */
    LOWER_CAMEL,

    /**
     * Upper camel: UpperCamel
     */
    UPPER_CAMEL,

    /**
     * Upper underscore: UPPER_UNDERSCORE
     */
    UPPER_UNDERSCORE;

    /**
     * Convert a string from the current format to the target format
     *
     * @param target The target case format
     * @param input The input string in the current format
     * @return The converted string in the target format
     */
    fun to(target: CaseFormat, input: String): String {
        if (input.isEmpty() || this == target) return input
        return splitToWords(input).joinAsTargetFormat(target)
    }

    /**
     * Split the input string into a list of words according to the current format
     *
     * @param input The input string
     * @return A list of words
     */
    private fun splitToWords(input: String): List<String> {
        return when (this) {
            LOWER_HYPHEN -> input.split("-")
            LOWER_UNDERSCORE, UPPER_UNDERSCORE -> input.split("_")
            LOWER_CAMEL, UPPER_CAMEL -> {
                val words = mutableListOf<String>()
                val currentWord = StringBuilder()

                for (c in input) {
                    if (c.isUpperCase()) {
                        if (currentWord.isNotEmpty()) {
                            words.add(currentWord.toString())
                            currentWord.clear()
                        }
                        currentWord.append(c.lowercaseChar())
                    } else {
                        currentWord.append(c)
                    }
                }
                if (currentWord.isNotEmpty()) {
                    words.add(currentWord.toString())
                }
                words
            }
        }.filter { it.isNotEmpty() }
    }

    /**
     * Join the list of words into a string according to the target format
     *
     * @param target The target case format
     * @return The joined string
     */
    private fun List<String>.joinAsTargetFormat(target: CaseFormat): String {
        if (isEmpty()) return ""

        return when (target) {
            LOWER_HYPHEN -> joinToString("-").lowercase()
            LOWER_UNDERSCORE -> joinToString("_").lowercase()
            UPPER_UNDERSCORE -> joinToString("_").uppercase()
            LOWER_CAMEL -> first().lowercase() +
                drop(1).joinToString("") { it.capitalize() }
            UPPER_CAMEL -> joinToString("") { it.capitalize() }
        }
    }

    /**
     * Capitalize the first letter of the string and lowercase the rest
     */
    private fun String.capitalize(): String {
        if (isEmpty()) return this
        return this[0].uppercaseChar().toString() + substring(1).lowercase()
    }

}
