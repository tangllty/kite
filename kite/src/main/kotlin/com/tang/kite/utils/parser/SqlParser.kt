package com.tang.kite.utils.parser

import com.tang.kite.annotation.Param
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.expression.ExpressionParser
import java.lang.reflect.Parameter

/**
 * SQL Parser for dynamic SQL with conditional logic and parameter binding.
 *
 * Supports:
 * - Dynamic parameter binding using #{paramName} syntax
 * - Conditional SQL blocks using if/else if/else statements
 * - Nested property access via dot notation (e.g., user.name)
 * - Automatic WHERE clause injection for orphaned AND/OR conditions
 *
 * @author Tang
 */
object SqlParser {

    /**
     * Parses SQL with parameters and default beautification enabled.
     *
     * @param sql SQL template string with placeholders and conditional logic
     * @param params Parameter map containing values for placeholders
     * @return Parsed and prepared SQL statement with parameter list
     */
    fun parse(sql: String, params: Map<String, Any?>): SqlStatement {
        return parse(sql, params, true)
    }

    /**
     * Parses SQL with parameters and optional beautification.
     *
     * @param sql SQL template string with placeholders and conditional logic
     * @param params Parameter map containing values for placeholders
     * @param beautify Whether to beautify the resulting SQL
     * @return Parsed and prepared SQL statement with parameter list
     * @throws IllegalArgumentException if SQL is blank or invalid
     */
    fun parse(sql: String, params: Map<String, Any?>, beautify: Boolean): SqlStatement {
        require(sql.isNotBlank()) { "SQL must not be blank." }

        val (rawSql, paramList) = parseSql(sql, params)
        val finalSql = autoAppendWhereIfNeeded(rawSql)

        require(isValidSql(finalSql)) { "SQL syntax error: invalid SQL, content: $finalSql" }

        val cleanedSql = if (beautify) beautifySql(finalSql) else cleanSql(finalSql)
        return SqlStatement(cleanedSql, paramList)
    }

    /**
     * Builds a comprehensive parameter map from method parameters and arguments.
     *
     * Supports multiple parameter naming strategies:
     * 1. @Param annotation: explicitly named parameters
     * 2. Indexed parameters: param1, param2, param3, etc.
     * 3. Single Map argument: all map entries are added
     * 4. Single object argument: all object fields are reflectively extracted
     *
     * @param parameters Method parameter metadata
     * @param args Actual argument values passed to the method
     * @return Map of parameter names to their values
     */
    fun buildParamValueMap(parameters: Array<Parameter>, args: Array<out Any>?): Map<String, Any?> {
        if (args.isNullOrEmpty()) {
            return emptyMap()
        }

        val map = mutableMapOf<String, Any?>()

        // Add indexed and annotated parameters
        parameters.forEachIndexed { index, param ->
            val annotation = param.getAnnotation(Param::class.java)
            if (annotation != null) {
                map[annotation.value] = args[index]
            }
            // Always add indexed parameter format
            map["param${index + 1}"] = args[index]
        }

        // Special handling for single argument
        if (args.size == 1) {
            when (val singleArg = args.first()) {
                is Map<*, *> -> {
                    // Merge all String-keyed map entries
                    singleArg.forEach { (key, value) ->
                        if (key is String) {
                            map[key] = value
                        }
                    }
                }
                is Any -> {
                    // Handle entity objects: extract all fields via reflection
                    if (Reflects.isEntity(singleArg)) {
                        val clazz = singleArg.javaClass
                        val fields = Reflects.getFields(clazz)
                        fields.forEach { field ->
                            Reflects.makeAccessible(field, singleArg)
                            map[field.name] = Reflects.getValue(singleArg, field.name)
                        }
                    }
                }
            }
        }
        return map
    }

    /**
     * Parses SQL template, processes conditional blocks and parameter placeholders.
     *
     * - Replaces #{paramName} with ? and collects parameter values
     * - Evaluates if/else if/else conditional blocks
     * - Validates balanced parentheses
     *
     * @param sql SQL template string
     * @param params Parameter values map
     * @return Pair of processed SQL string and ordered parameter list
     * @throws IllegalArgumentException if syntax errors detected
     */
    private fun parseSql(sql: String, params: Map<String, Any?>): Pair<String, MutableList<Any?>> {
        val sqlBuffer = StringBuilder(sql.length)
        val paramList = mutableListOf<Any?>()
        var i = 0
        var parenCount = 0

        while (i < sql.length) {
            val c = sql[i]

            // Check for conditional 'if' block
            if (c == 'i' && startsWithIf(sql, i)) {
                val (ifSql, ifParams, endIdx) = parseIfBlock(sql, i, params)
                sqlBuffer.append(ifSql)
                paramList.addAll(ifParams)
                i = endIdx
                continue
            }

            when (c) {
                '(' -> {
                    parenCount++
                    sqlBuffer.append(c)
                }
                ')' -> {
                    parenCount--
                    require(parenCount >= 0) { "SQL syntax error: Unmatched ')' at position $i" }
                    sqlBuffer.append(c)
                }
                '#' if i + 1 < sql.length && sql[i + 1] == '{' -> {
                    // Extract parameter placeholder
                    val (paramName, end) = extractPlaceholder(sql, i)
                    sqlBuffer.append("?")
                    paramList.add(resolveParam(paramName, params, i))
                    i = end - 1
                }
                else -> sqlBuffer.append(c)
            }
            i++
        }

        require(parenCount == 0) { "SQL syntax error: unmatched '(' or ')' in SQL." }

        return sqlBuffer.toString() to paramList
    }

    /**
     * Skips all else if/else branches after a matched if condition.
     *
     * When an if condition evaluates to true, we need to skip all subsequent
     * else if and else branches to avoid duplicate SQL generation.
     *
     * @param sql Complete SQL string
     * @param startIdx Starting position after matched if block
     * @return Index position after all skipped else branches
     */
    private fun skipElseBranches(sql: String, startIdx: Int): Int {
        var i = startIdx

        while (i < sql.length) {
            i = getNextNonWhitespaceIndex(sql, i)
            if (i >= sql.length || !startsWithElse(sql, i)) {
                break
            }

            // Skip 'else' keyword (4 characters)
            i += 4
            i = getNextNonWhitespaceIndex(sql, i)

            // Handle 'else if' blocks
            if (startsWithIf(sql, i)) {
                i += 2 // Skip 'if'
                i = getNextNonWhitespaceIndex(sql, i)

                // Skip condition parentheses
                if (i < sql.length && sql[i] == '(') {
                    i = skipBalancedParentheses(sql, i)
                }
                i = getNextNonWhitespaceIndex(sql, i)
            }

            // Skip the body braces
            if (i < sql.length && sql[i] == '{') {
                i = skipBalancedBraces(sql, i)
            }
        }
        return i
    }

    /**
     * Parses conditional if/else if/else block in SQL template.
     *
     * Syntax: if(condition) { sql } else if(condition) { sql } else { sql }
     *
     * @param sql Complete SQL string
     * @param startIdx Starting position of 'if' keyword
     * @param params Parameter values for condition evaluation
     * @return Triple of (generated SQL, parameter list, next index)
     * @throws IllegalArgumentException if syntax errors detected
     */
    private fun parseIfBlock(sql: String, startIdx: Int, params: Map<String, Any?>): Triple<String, List<Any?>, Int> {
        var i = startIdx + 2 // Skip 'if'
        i = getNextNonWhitespaceIndex(sql, i)

        require(i < sql.length && sql[i] == '(') {
            "SQL syntax error: 'if' must be followed by '(' at position $startIdx"
        }

        // Extract condition from parentheses
        i++ // Skip '('
        val condStart = i
        i = skipBalancedParentheses(sql, i - 1)
        val condEnd = i - 1
        val condition = sql.substring(condStart, condEnd).trim()

        require(condition.isNotEmpty()) {
            "SQL syntax error: empty condition in if statement at position $startIdx"
        }

        // Extract SQL body from braces
        i = getNextNonWhitespaceIndex(sql, i)
        require(i < sql.length && sql[i] == '{') {
            "SQL syntax error: 'if' must be followed by '{' after condition at position $startIdx"
        }

        i++ // Skip '{'
        val sqlStart = i
        i = skipBalancedBraces(sql, i - 1)
        val sqlEnd = i - 1
        val innerSql = sql.substring(sqlStart, sqlEnd)

        // Evaluate condition
        val condResult = try {
            ExpressionParser.evaluateBoolean(condition, params)
        } catch (e: Exception) {
            throw IllegalArgumentException("SQL syntax error: failed to evaluate if condition '$condition' at position $startIdx: ${e.message}", e)
        }

        // If condition is true, parse inner SQL and skip else branches
        if (condResult) {
            val (innerParsedSql, innerParams) = parseSql(innerSql, params)
            val nextIdx = skipElseBranches(sql, i)
            return Triple(innerParsedSql, innerParams, nextIdx)
        }

        // Process else if/else branches
        var j = i
        while (j < sql.length) {
            j = getNextNonWhitespaceIndex(sql, j)
            if (j >= sql.length || !startsWithElse(sql, j)) {
                break
            }

            j += 4 // Skip 'else'
            j = getNextNonWhitespaceIndex(sql, j)

            // Handle 'else if' recursively
            if (startsWithIf(sql, j)) {
                return parseIfBlock(sql, j, params)
            }

            // Handle plain 'else' block
            require(j < sql.length && sql[j] == '{') {
                "SQL syntax error: 'else' must be followed by '{' at position $j"
            }

            j++ // Skip '{'
            val elseStart = j
            j = skipBalancedBraces(sql, j - 1)
            val elseEnd = j - 1
            val elseSql = sql.substring(elseStart, elseEnd)
            val (elseParsedSql, elseParams) = parseSql(elseSql, params)
            return Triple(elseParsedSql, elseParams, j)
        }

        // No condition matched, return empty
        return Triple("", emptyList(), i)
    }

    /**
     * Automatically prepends WHERE keyword to orphaned AND/OR conditions.
     *
     * Handles cases where conditional blocks result in AND/OR at the start
     * of conditions without a WHERE clause.
     *
     * Example:
     *   "SELECT * FROM user AND age > 18" → "SELECT * FROM user WHERE age > 18"
     *
     * @param sql SQL string to process
     * @return SQL with WHERE added if needed
     */
    private fun autoAppendWhereIfNeeded(sql: String): String {
        val lower = sql.lowercase()
        if (Regex("\\bwhere\\b").containsMatchIn(lower)) {
            return sql
        }

        // Keywords that should not be preceded by WHERE
        val forbidden = setOf("order", "group", "having", "limit")
        val lines = sql.lines().toMutableList()
        var firstCondIdx = -1

        // Find first line starting with AND/OR that's not in a forbidden clause
        for ((idx, line) in lines.withIndex()) {
            val trimmed = line.trimStart().lowercase()
            val firstWord = Regex("^\\s*(\\w+)").find(trimmed)?.groupValues?.get(1) ?: ""

            if ((trimmed.startsWith("and ") || trimmed.startsWith("or ")) && firstWord !in forbidden) {
                firstCondIdx = idx
                break
            }
        }

        if (firstCondIdx != -1) {
            // Replace AND/OR with WHERE
            val condLine = lines[firstCondIdx]
            val cleaned = condLine.trimStart().replace(Regex("^(and|or)\\s+", RegexOption.IGNORE_CASE), "")
            lines[firstCondIdx] = "where $cleaned"
            return lines.joinToString("\n")
        }

        return sql
    }

    /**
     * Extracts parameter name from #{paramName} placeholder.
     *
     * @param sql Complete SQL string
     * @param startIdx Starting position of '#' character
     * @return Pair of parameter name and ending position
     * @throws IllegalArgumentException if placeholder is malformed
     */
    private fun extractPlaceholder(sql: String, startIdx: Int): Pair<String, Int> {
        var end = startIdx + 2 // Skip '#{
        var braceCount = 1

        while (end < sql.length && braceCount > 0) {
            when (sql[end]) {
                '{' -> braceCount++
                '}' -> braceCount--
            }
            end++
        }

        require(braceCount == 0) {
            "SQL syntax error: unmatched #{} placeholder at position $startIdx"
        }

        val paramName = sql.substring(startIdx + 2, end - 1).trim()
        require(paramName.isNotEmpty()) {
            "SQL syntax error: empty parameter name in #{} at position $startIdx"
        }

        return paramName to end
    }

    /**
     * Resolves parameter value from map, supporting nested property access.
     *
     * Examples:
     *   "name" → params["name"]
     *   "user.name" → params["user"].name (via reflection)
     *   "order.items[0].price" → params["order"].items[0].price (via reflection)
     *   "ids[0]" → params["ids"][0] (array element access)
     *   "users[1].name" → params["users"][1].name (array element with property access)
     *
     * @param paramName Parameter name, possibly with dot notation or array indexing
     * @param params Parameter values map
     * @param pos Position in SQL for error reporting
     * @return Resolved parameter value
     * @throws IllegalArgumentException if parameter not found
     */
    private fun resolveParam(paramName: String, params: Map<String, Any?>, pos: Int): Any? {
        return try {
            ExpressionParser.evaluate(paramName, params)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to resolve parameter '$paramName' at position $pos: ${e.message}", e)
        }
    }

    /**
     * Beautifies SQL by normalizing whitespace and operator spacing.
     *
     * Operations:
     * - Collapses multiple whitespace to single space
     * - Adds spacing around operators (=, >, <, >=, <=, etc.)
     * - Normalizes parentheses spacing
     *
     * @param sql SQL to beautify
     * @return Beautified SQL string
     */
    private fun beautifySql(sql: String): String {
        var beautified = sql.replace(Regex("\\s+"), " ").trim()

        // Add spacing around operators
        val operators = listOf(">=", "<=", "<>", "!=", "=", ">", "<", "\\+", "\\*", "/")
        for (operator in operators) {
            beautified = beautified.replace(Regex("\\s*($operator)\\s*"), " $1 ")
        }

        // Normalize parentheses spacing
        beautified = beautified
            .replace(Regex("\\(\\s+"), "(")
            .replace(Regex("\\s+\\)"), ")")
            .replace(Regex("\\s+\\("), " (")
            .replace(Regex("\\)\\s+"), ") ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return beautified
    }

    /**
     * Cleans SQL by removing trailing whitespace and blank lines.
     *
     * @param sql SQL to clean
     * @return Cleaned SQL with blank lines removed
     */
    private fun cleanSql(sql: String): String =
        sql.lines()
            .map { it.trimEnd() }
            .filter { it.isNotBlank() }
            .joinToString("\n")

    /**
     * Validates SQL structure and syntax.
     *
     * Checks:
     * - Starts with valid SQL command (SELECT, INSERT, UPDATE, DELETE)
     * - Contains at least one letter
     * - Only contains valid SQL characters
     *
     * @param sql SQL to validate
     * @return true if SQL appears valid
     */
    private fun isValidSql(sql: String): Boolean {
        val cleaned = removeIfBlocks(sql).trim()

        // Must start with valid SQL command
        val validStart = Regex("^(select|insert|update|delete)\\b", RegexOption.IGNORE_CASE)
        if (!validStart.containsMatchIn(cleaned)) {
            return false
        }

        // Must contain at least one letter
        if (!Regex("[a-zA-Z]").containsMatchIn(cleaned)) {
            return false
        }

        // Only allow valid SQL characters
        if (Regex("[^\\w\\s,=><!*()?.'\"-_%]").containsMatchIn(cleaned)) {
            return false
        }

        return true
    }

    /**
     * Removes all if/else blocks from SQL for validation purposes.
     *
     * This allows validating the SQL structure without conditional blocks.
     *
     * @param sql SQL with potential if blocks
     * @return SQL with all if blocks removed
     */
    private fun removeIfBlocks(sql: String): String {
        val sb = StringBuilder(sql.length)
        var i = 0

        while (i < sql.length) {
            if (startsWithIf(sql, i)) {
                var j = i + 2
                j = getNextNonWhitespaceIndex(sql, j)

                if (j < sql.length && sql[j] == '(') {
                    // Skip condition
                    j = skipBalancedParentheses(sql, j)
                    j = getNextNonWhitespaceIndex(sql, j)

                    if (j < sql.length && sql[j] == '{') {
                        // Skip body
                        j = skipBalancedBraces(sql, j)
                        i = j
                        continue
                    }
                }
            }
            sb.append(sql[i])
            i++
        }
        return sb.toString()
    }

    /**
     * Skips balanced delimiters (parentheses or braces) starting from given position.
     *
     * @param sql SQL string
     * @param startIdx Position of opening delimiter
     * @param openChar Opening delimiter character
     * @param closeChar Closing delimiter character
     * @return Position after closing delimiter
     */
    private fun skipBalancedDelimiters(sql: String, startIdx: Int, openChar: Char, closeChar: Char): Int {
        var i = startIdx + 1
        var count = 1
        while (i < sql.length && count > 0) {
            when (sql[i]) {
                openChar -> count++
                closeChar -> count--
            }
            i++
        }
        return i
    }

    /**
     * Skips balanced parentheses starting from given position.
     *
     * @param sql SQL string
     * @param startIdx Position of opening '('
     * @return Position after closing ')'
     */
    private fun skipBalancedParentheses(sql: String, startIdx: Int): Int {
        return skipBalancedDelimiters(sql, startIdx, '(', ')')
    }

    /**
     * Skips balanced braces starting from given position.
     *
     * @param sql SQL string
     * @param startIdx Position of opening '{'
     * @return Position after closing '}'
     */
    private fun skipBalancedBraces(sql: String, startIdx: Int): Int {
        return skipBalancedDelimiters(sql, startIdx, '{', '}')
    }

    /**
     * Finds the next non-whitespace character position.
     *
     * @param sql SQL string
     * @param startIdx Starting position
     * @return Position of next non-whitespace character
     */
    private fun getNextNonWhitespaceIndex(sql: String, startIdx: Int): Int {
        var i = startIdx
        while (i < sql.length && sql[i].isWhitespace()) {
            i++
        }
        return i
    }

    /**
     * Checks if SQL starts with 'if' keyword at given position (case-insensitive).
     */
    private fun startsWithIf(sql: String, index: Int): Boolean {
        return sql.startsWith("if", index, ignoreCase = true)
    }

    /**
     * Checks if SQL starts with 'else' keyword at given position (case-insensitive).
     */
    private fun startsWithElse(sql: String, index: Int): Boolean {
        return sql.startsWith("else", index, ignoreCase = true)
    }

}
